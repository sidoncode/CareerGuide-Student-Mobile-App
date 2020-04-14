package com.careerguide.blog.api;

import com.careerguide.blog.model.Categories;
import com.careerguide.blog.model.CategoryDetails;

import java.util.List;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiService {
    @GET("cat_list.php")
    Single<List<Categories>> get_categories(@Query("per_page") String per_page, @Query("page") String page);

    @GET("cat_detail.php")
    Single<List<CategoryDetails>> get_cat_detail(@Query("per_page") String per_page, @Query("page") String page);

    @GET("cat_detail_wo.php")
    Single<List<CategoryDetails>> get_cat_detail_wo(@Query("per_page") String per_page, @Query("page") String page);

    @GET("cat_detail.php")
    Single<List<CategoryDetails>> get_specific_cat_detail(@Query("categories") String categories, @Query("per_page") String per_page, @Query("page") String page);

    @GET("cat_detail_by_id.php")
    Single<List<CategoryDetails>> get_specific_cat_by_id(@Query("id") String id);
}