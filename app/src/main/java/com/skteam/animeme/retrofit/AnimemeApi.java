package com.skteam.animeme.retrofit;

import com.skteam.animeme.models.ImageDataResponse;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface AnimemeApi {

    @FormUrlEncoded
    @POST("get_image_category_list.php")
    Call<ImageDataResponse> getImageCategoryList(@Field("Type") String type);

    @GET("get_meme_list.php")
    Call<ImageDataResponse> getMemesList();

    @GET("get_template_list.php")
    Call<ImageDataResponse> getTemplatesList();

    @FormUrlEncoded
    @POST("get_meme_list_by_category.php")
    Call<ImageDataResponse> getMemeListByCategory(@Field("Category") String category);

    @FormUrlEncoded
    @POST("get_template_list_by_category.php")
    Call<ImageDataResponse> getTemplateListByCategory(@Field("Category") String category);

    @GET("get_random_list.php")
    Call<ImageDataResponse> getRandomList();
}
