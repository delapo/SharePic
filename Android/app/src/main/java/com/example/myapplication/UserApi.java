package com.example.myapplication;


import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.Call;
import retrofit2.http.Part;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;

//@Part MultipartBody.Part photo,


public interface UserApi {

    @POST("auth/create")
    Call<String> postUser(@Body User user);

    @FormUrlEncoded
    @POST("user/abo")
    Call<Integer> follow(@Header("Authorization") String token,
                        @Field("user_id") Integer user_id);

    @FormUrlEncoded
    @POST("user/desabo")
    Call<Integer> unfollow(@Header("Authorization") String token,
                         @Field("user_id") Integer user_id);


    @FormUrlEncoded
    @POST("like_pictures/add")
    Call<Integer> like(@Header("Authorization") String token,
                         @Field("pictures_id") Integer pictures_id);

    @FormUrlEncoded
    @POST("like_pictures/delete")
    Call<Integer> dislike(@Header("Authorization") String token,
                           @Field("like_id") Integer like_id);



    @POST("auth/connect")
    Call<String> postLogin(@Body Login login);

    @GET("user/search_users")
    Call<List<User>> searchCall(@Header("Authorization")String token,
                                @Query("search") String searchString);

    @Multipart
        @POST("user/create_profil_pic")
        Call<String> uploadImage(@Header("Authorization") String token,
                                 @Part MultipartBody.Part profil_picture);
    @GET("user/getabonbr")
    Call<Integer> getFollowNbr(@Header("Authorization") String token);

    @GET("user/getabonnementnbr")
    Call<Integer> getFollowingNbr(@Header("Authorization") String token);

    @GET("user/find/getName")
    Call<String> getUserName(@Header("Authorization") String token);

    @GET("pictures/myprofilpic")
    Call<JsonObject> getMyProfilPic(@Header("Authorization") String token);

    @GET("pictures/fil")
    Call<List<ArrayList>> getPicturesFil(@Header("Authorization") String token);

    @GET("pictures/getmyall")
    Call<ArrayList> getmypost(@Header("Authorization") String token);


    @GET("pictures/getall/{id}")
    Call<ArrayList> getpost(@Header("Authorization") String token,
                            @Path("id") Integer id);



    @FormUrlEncoded
    @POST("user/changeInformation")
    Call<String> setInformation(
            @Header("Authorization") String token,
            @Field("username") String username,
            @Field("password") String password,
            @Field("confirm_password") String confirm_password );

    @Multipart
    @POST("pictures/add")
    Call<ResponseBody> uploadPicture(@Header("Authorization") String token,
                                     @PartMap() Map<String, RequestBody> map,
                                     @Part MultipartBody.Part photo
    );

    @GET("user/userName/{username}")
    Call<JsonObject> getUser(@Header("Authorization") String token,
                             @Path("username") String username);

    @GET("user/getabonbr/{id}")
    Call<Integer> getFollowNbrid(@Header("Authorization") String token,
                                 @Path("id") Integer id);


    @GET("user/getabonnementnbr/{id}")
    Call<Integer> getFollowingNbrid(@Header("Authorization") String token,
                                    @Path("id") Integer id);

    @GET("pictures/NbrPic/{id}")
    Call<Integer> getPicNbr(@Header("Authorization") String token,
                                    @Path("id") Integer id);

    @GET("pictures/NbrMyPic")
    Call<Integer> getMyPicNbr(@Header("Authorization") String token);

    @GET("pictures/profillien/{id}")
    Call<JsonObject> getProfilPicId(@Header("Authorization") String token,
                                 @Path("id") Integer id);

    @GET("user/ifabo/{id}")
    Call<JsonObject> ifabo(@Header("Authorization") String token,
                           @Path("id") Integer id);

    @GET("pictures/iflike/{id}")
    Call<JsonObject> iflike(@Header("Authorization") String token,
                           @Path("id") Integer id);


    @GET("like_pictures/nbr/{id}")
    Call<Integer> nbrlike(@Header("Authorization") String token,
                           @Path("id") Integer id);

    @GET("comments/nbr/{id}")
    Call<Integer> nbrcom(@Header("Authorization") String token,
                           @Path("id") Integer id);

    @GET("user/find/getUSERName/{id}")
    Call<String> getausername(@Header("Authorization") String token,
                         @Path("id") Integer id);

    @GET("/api/comments/commentsFromPicture")
    Call<ArrayList> getComments(@Header("Authorization") String token,
                                @Query("picture_id") int id);
    @GET("user/getabo/{id}")
    Call<ArrayList> getFollowers(@Header("Authorization") String token,
                                 @Path("id") Integer id);
    @GET("user/getProfilPicTo/{id}")
    Call<JsonObject> getProfilPictureTo(@Header("Authorization") String token,
                                        @Path("id") Integer id);

    @FormUrlEncoded
    @POST("comments/add")
    Call<String> sendComment(@Header("Authorization") String token,
                             @Field("picture_id") int id,
                             @Field("date") String date,
                             @Field("comment") String comment );

    @GET("user/getaboauth")
    Call<ArrayList> getAbonne(@Header("Authorization") String token);

    @GET("user/getabonnementauth")
    Call<ArrayList> getAbonnement(@Header("Authorization") String token);
}