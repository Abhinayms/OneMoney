package com.sevya.onemoney.service.impl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.google.gson.Gson;
import com.sevya.launchpad.error.ResourceNotFoundException;
import com.sevya.launchpad.model.User;
import com.sevya.onemoney.dto.CAASCategoryRequestDto;
import com.sevya.onemoney.dto.CAASCategoryResponseDto;
import com.sevya.onemoney.dto.CAASDto;
import com.sevya.onemoney.dto.CategoryDto;
import com.sevya.onemoney.dto.ResponseDto;
import com.sevya.onemoney.dto.mapper.CAASDtoMapper;
import com.sevya.onemoney.dto.mapper.CategoryDtoMapper;
import com.sevya.onemoney.model.Category;
import com.sevya.onemoney.model.CategoryTransactionMapper;
import com.sevya.onemoney.repository.CategoryRepository;
import com.sevya.onemoney.repository.CategoryTransactionMapperRepository;
import com.sevya.onemoney.service.CategoryService;
import com.sevya.onemoney.utility.DateUtility;
import com.sevya.onemoney.utility.OneMoneyConstants;

@Service
@PropertySource(value = { "classpath:constants.properties" })
public class CategoryServiceImpl implements CategoryService{

	@Autowired
	private CategoryRepository catagoryRepository;
	
	@Autowired
	private CategoryTransactionMapperRepository categoryTransactionMapperRepository;
	
	@Autowired
	private Environment env;
	
	@Override
	public CategoryDto addCategory(CategoryDto dto) {
		Category catagory = CategoryDtoMapper.toCategory(new Category(),dto);
		catagory = catagoryRepository.save(catagory);
		CategoryDto dtoObj = new CategoryDto();
		dtoObj.setCategoryCode(catagory.getUuid());
		return dtoObj;
	}

	@Override
	public void updateCategory(CategoryDto dto) {
		Category catagory = catagoryRepository.getCategoryByUuid(dto.getCategoryCode());
		if(catagory == null){
			throw new ResourceNotFoundException();
		}else{
			catagory = CategoryDtoMapper.toCategory(catagory, dto);
			catagoryRepository.save(catagory);
		}
	}

	@Override
	public void deleteCategoryByUuid(String uuid) {
		Category category = catagoryRepository.getCategoryByUuid(uuid);
		if(category != null){
			catagoryRepository.deleteCategory(category.getId());
		}else{
			throw new NullPointerException();
		}
	}

	@Override
	public ResponseDto getCategories(User user) throws Exception {
		
		ResponseDto responseDto = new ResponseDto();
		
		if(user == null){
			throw new ResourceNotFoundException("User doesn't exists...!!!");
		}
			
		List<Category> categories = catagoryRepository.getCategories(user.getId(),OneMoneyConstants.Super_Admin_User_Id);
		List<CategoryDto> dtos = new ArrayList<>();
			
		if(!categories.isEmpty()){
			for(Category category : categories){
				if(category.getParentId() == null){
					List<Category> subCategories = catagoryRepository.getSubCategoriesByParentId(user.getId(),category.getId(),OneMoneyConstants.Super_Admin_User_Id);
					CategoryDto dto = CategoryDtoMapper.toCategoryDto(new CategoryDto(), category, subCategories);
					dtos.add(dto);
				}
			}
			
			Category category = catagoryRepository.getLastUpdatedDateForCategory(user.getId(),OneMoneyConstants.Super_Admin_User_Id);
			Date latestDate;
			
			if(category == null){
				responseDto.setLastUpdatedDateForCategory("N/A");
			}else{
				if(category.getModifiedDate() != null && category.getCreatedDate().compareTo(category.getModifiedDate()) < 0){
					latestDate = category.getModifiedDate();
				}else{
					latestDate = category.getCreatedDate();
				}
				responseDto.setLastUpdatedDateForCategory(DateUtility.DATE_FORMATTER_FROM_STRING_TO_DATE.format(latestDate));
			}
			
			responseDto.setCategories(dtos);
			return responseDto;
		}else{
			return null;
		}
		
	}
	
	@Override
	@Transactional(rollbackFor = Exception.class)
	public CategoryDto addSubCategory(User user,CategoryDto dto) throws Exception {
		Category catagory = catagoryRepository.getCategoryByUuid(dto.getParentCategoryCode());
		if(catagory == null){
			throw new ResourceNotFoundException();
		} else {
			
			Category subCatagory = CategoryDtoMapper.toCategory(new Category(), dto);
			
			CAASDto caasDto = CAASDtoMapper.toCAASDto(dto);
			CAASCategoryRequestDto caasCategoryRequestDto = new CAASCategoryRequestDto();
			caasCategoryRequestDto.setData(caasDto);
			CAASCategoryResponseDto responseDto = addSubCategoryInCASS(user, caasCategoryRequestDto);
			caasDto = responseDto.getPayload();
			
			subCatagory.setUuid(caasDto.getCategory_id());
			subCatagory.setParentId(catagory.getId());
			subCatagory.setUserCreated(true);
			subCatagory = catagoryRepository.save(subCatagory);
			CategoryDto dtoObj = new CategoryDto();
			dtoObj.setCategoryCode(subCatagory.getUuid());
			dtoObj.setCategoryName(subCatagory.getName());
			dtoObj.setIsUserCreated(subCatagory.getUserCreated());
			return dtoObj;
		}
	}

	
	
	
	public CAASCategoryResponseDto addSubCategoryInCASS(User user,CAASCategoryRequestDto caasCategoryRequestDto) throws RestClientException {
		
		CAASCategoryResponseDto caasCategoryResponseDto = null;
		
		try {
			
			String url = "https://sandbox-api.ewise.com/caas/fintech_solutions_in/"+user.getUuid()+"/create_label";
			String token = env.getProperty("constans.CAAS_server_token");
			RestTemplate restTemplate = new RestTemplate();
			HttpHeaders headers = new HttpHeaders();
			headers.add("Authentication-Token",token);
			headers.add("Content-Type","application/json");
			HttpEntity<CAASCategoryRequestDto> request = new HttpEntity<>(caasCategoryRequestDto,headers);
			ResponseEntity<CAASCategoryResponseDto> response = restTemplate.exchange(url, HttpMethod.POST, request, CAASCategoryResponseDto.class);
			caasCategoryResponseDto = response.getBody();
			
			if(caasCategoryResponseDto.getStatus() == 200) {
				return  caasCategoryResponseDto;
			} else {
				 throw new RestClientException(caasCategoryResponseDto.getMessage());
			}
		
		} catch (HttpStatusCodeException exception) {
			
			 caasCategoryResponseDto = new Gson().fromJson(exception.getResponseBodyAsString(), CAASCategoryResponseDto.class);
			 throw new RestClientException(caasCategoryResponseDto.getMessage());
			 
		}
	
}
	
	
	
	@Override
	public CategoryDto updateSubCategory(CategoryDto dto) throws Exception {
		Category catagory = catagoryRepository.getCategoryByUuid(dto.getCategoryCode());
		if(catagory == null){
			throw new ResourceNotFoundException();
		}else{
			catagory = CategoryDtoMapper.toCategory(catagory, dto);
			catagoryRepository.save(catagory);
			CategoryDto dtoObj = new CategoryDto();
			dtoObj.setCategoryCode(catagory.getUuid());
			dtoObj.setCategoryName(catagory.getName());
			dtoObj.setIsUserCreated(catagory.getUserCreated());
			return dtoObj;
			
		}
	}

	@Override
	public Category getCategoryById(Long id) {
		return catagoryRepository.getCategory(id);
	}

	@Override
	public List<Category> getSubCategoriesByCategoryId(User user,Long categoryId) {
		return catagoryRepository.getSubCategoriesByParentId(user.getId(),categoryId,OneMoneyConstants.Super_Admin_User_Id);
	}
	
	@Override
	public Category getCategoryByUuid(String code) {
		return catagoryRepository.getCategoryByUuid(code);
	}

	
	@Override
	public Category getCategoryByName(String name) {
		return catagoryRepository.getCategoryByName(name);
	}
	
	@Override
	public Float getTotalAmountForCategory(Long userId, String categoryName, Integer month, Integer year) {
		return categoryTransactionMapperRepository.getToatalAmountForCategory(userId,categoryName, month, year);
	}
	
	
	@Override
	public Float getTotalSpentForCurrentMonthByCategoryName(Long userId, String categoryName, Integer month, Integer year) {
		return categoryTransactionMapperRepository.getTotalSpentForUserByMonthByUserIdAndCategoryNameAndMonthAndYear(userId,categoryName, month, year);
	}
	
	@Override
	public Float getAverageSpentForLastThreeMonthsByCategoryName(Long userId, String categoryName, String pastmonth, String currentMonth) {
		return categoryTransactionMapperRepository.getAverageSpentByUserLastThreeMonthsByUserIdAndCategoryNameAndMonthAndYear(userId,categoryName, pastmonth, currentMonth);
	}
	
	
	@Override
	public Float getTotalSpentForCurrentMonthByCategoryId(Long userId,Long categoryId, String categoryName, Integer month, Integer year) {
		return categoryTransactionMapperRepository.getTotalSpentForCurrentMonthByUserIdAndCategoryIdAndMonthAndYear(userId,categoryId,categoryName, month, year);
	}
	
	@Override
	public Float getAverageSpentForLastThreeMonthsByCategoryId(Long userId,Long categoryId, String categoryName, String pastmonth, String currentMonth) {
		return categoryTransactionMapperRepository.getAverageSpentForLastThreeMonthsByUserIdAndCategoryIdAndMonthAndYear(userId,categoryId,categoryName, pastmonth, currentMonth);
	}

	@Override
	public List<Category> getAllCategoryList() {
		return catagoryRepository.getAllCategoryList();
	}

	@Override
	public Float totalSpentsForUserByPurposeCategoryMonthAndYear(Long userId, String purpose, String categoryName,Integer month, Integer year) {
		return categoryTransactionMapperRepository.getTotalSpentsForUserByPurposeCategoryMonthAndYear(userId, purpose, categoryName, month, year);
	}
	
	@Override
	public Integer getUnCategorizedTransactionsCount(Long userId, String purpose,Integer month, Integer year){
		return categoryTransactionMapperRepository.getUnCategorizedTransactionsCount(userId, purpose, month, year);
	}

	@Override
	public List<Object[]> getTotalIncomingAndTotalOutgoingAmountsForAnAccountOfUser(Long userId, String accountId, Integer month, Integer year) {
		return categoryTransactionMapperRepository.getTotalIncomingAndTotalOutgoingByUserIdAndAccountCodeAndMonthAndYear(userId, accountId, month, year);
	}

	@Override
	public boolean deleteCategoryTransactionMappersByUserIdAndAccountId(Long userId, Long accountId) {
		
		boolean isTxDeleted = false;
		
		List<CategoryTransactionMapper> categoryTransactionMappers = categoryTransactionMapperRepository.getCategoryTransactionMapperByUserIdAndAccountId(userId,accountId);
		List<CategoryTransactionMapper> modifiedObjectList = new ArrayList<>();
		if(!categoryTransactionMappers.isEmpty()) {
			categoryTransactionMappers.forEach(mapper -> {
				mapper.setIsActive(false);
				mapper.getTransaction().setIsActive(false);
				modifiedObjectList.add(mapper);
				
			});
			isTxDeleted = true;
		}
		categoryTransactionMapperRepository.save(modifiedObjectList);
		return isTxDeleted;
	}

	@Override
	public List<Object[]> getUnBudgedCategoriesByUserIdAndMonthAndYear(Long userId,String purpose,Integer month, Integer year) {
		return categoryTransactionMapperRepository.getUnBudgedCategoriesByUserIdAndMonthAndYear(userId,purpose,OneMoneyConstants.Income_Category,month,year);
	}

	@Override
	public List<Object[]> getUnBudgedSubCategoriesByUserIdAndMonthAndYearAndSubCatagoryIds(Long userId, String purpose,
			List<Long> subCategoryIds, Integer month, Integer year) {
		return categoryTransactionMapperRepository.getUnBudgedSubCategoriesByUserIdAndMonthAndYearAndSubCatagoryIds
				(userId, purpose, subCategoryIds, month, year);
	}

	@Override
	public List<BigInteger> getSubCategoryIdsByParentCategoryId(User user, String parentCategoryCode) {
		return catagoryRepository.getSubCategoryIdsByParentCategoryId(user.getId(),parentCategoryCode,OneMoneyConstants.Super_Admin_User_Id);
	}

	@Override
	public BigInteger getUnBudgetedCatagoriesAmountByUserIdAndCategoryIdandPurpose(Long userId, Long categoryId,String purpose,Integer month, Integer year) {
		return categoryTransactionMapperRepository.getUnBudgetedCatagoriesAmountByUserIdAndCategoryIdandPurpose(userId, categoryId, purpose,month,year);
	}
	
}