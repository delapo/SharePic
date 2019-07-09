<?php

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Lcobucci\JWT\Parser;
/*
|--------------------------------------------------------------------------
| API Routes
|--------------------------------------------------------------------------
|
| Here is where you can register API routes for your application. These
| routes are loaded by the RouteServiceProvider within a group which
| is assigned the "api" middleware group. Enjoy building your API!
|
*/

Route::middleware('auth:api')->get('/user', function (Request $request) {
    return $request->user();
});
Auth::routes();

Route::get('/auth/nbruser', 'Auth\RegisterController@usernbr')->name('usernbr');
Route::get('/auth/getauser/{id}', 'Auth\RegisterController@getauser')->name('getuser');

Route::post('/auth/create', 'Auth\RegisterController@cre')->name('cre');
Route::post('/auth/connect', 'Auth\RegisterController@connect')->name('add_user');
Route::get('/auth/list', 'Auth\RegisterController@lister')->name('lister');
Route::get('/auth/logout', 'Auth\RegisterController@logout')->name('logout');
Route::get('/', 'Controller@see')->name('see');

Route::group(['prefix' => 'user', 'middleware' => 'auth:api'], function () {
    Route::post('/create_profil_pic', 'UsersController@profilpic')->name('profilpic');
    Route::post('/abo', 'UsersController@Abo')->name('abo_user');
    Route::post('/desabo', 'UsersController@DesAbo')->name('desabo_user');
    Route::get('/getabo/{id}', 'UsersController@GetAbonne')->name('getabonbr');
    Route::get('/getabonbr', 'UsersController@Abonbr')->name('getabonnenbr');
    Route::get('/getabonnementnbr', 'UsersController@Abonnementnbr')->name('getabonnebr');
    Route::get('/getabonnement/{id}', 'UsersController@GetAbonnement')->name('getabonne');
    Route::get('/{id}', 'UsersController@findId')->name('findId');
    Route::get('/userName/{userName}', 'UsersController@findUsername')->name('findUsername');
});

Route::group(['prefix' => 'pictures', 'middleware' => 'auth:api'], function () {
    Route::post('/add', 'PicturesController@add')->name('add_pictures');
    Route::post('/delete', 'PicturesController@delete')->name('delete_pictures');
    Route::get('/change', 'PicturesController@change')->name('change_pictures');
    Route::get('/localisation/{id}', 'PicturesController@findPicLocalisation')->name('findIdLocalisaton');
    Route::get('/date/{id}', 'PicturesController@findPicDate')->name('findIdDate');
    Route::get('/lien/{id}', 'PicturesController@findPicFile')->name('findpicfile');
    Route::get('/profillien/{id}', 'PicturesController@findProfilFile')->name('findprofilfile');
    Route::get('/fil', 'PicturesController@Picoffollowers')->name('actualite');
    Route::get('/NbrPic/{id}', 'PicturesController@NbrPic')->name('nbrpic');
    Route::get('/get/{start}/{end}/{user_id}', 'PicturesController@show')->name('show_pictures');
    Route::get('/getall/{user_id}', 'PicturesController@showall')->name('show_picturesall');
});

Route::group(['prefix' => 'comments', 'middleware' => 'auth:api'], function () {
    Route::post('/add', 'CommentsController@add')->name('add_comments');
    Route::post('/delete', 'CommentsController@delete')->name('delete_comments');
    Route::get('/change', 'Comments Controller@change')->name('change_comments');
});

Route::group(['prefix' => 'like_comments', 'middleware' => 'auth:api'], function () {
    Route::post('/add', 'LikeController@add_comments')->name('add_like_comments');
    Route::post('/delete', 'LikeController@delete_comments')->name('delete_like_comments');
    Route::get('/nbr/{id}', 'LikeController@nbrlikecom')->name('nbrlikecom');
    Route::get('/change', 'LikeController@change_comments')->name('change_like_comments');
});

Route::group(['prefix' => 'like_pictures', 'middleware' => 'auth:api'], function () {
    Route::post('/add', 'LikeController@add_pictures')->name('add_like_pictures');
    Route::get('/nbr/{id}', 'LikeController@nbrlikepic')->name('nbrlikepic');
    Route::post('/delete', 'LikeController@delete_pictures')->name('delete_like_pictures');
    Route::get('/change', 'LikeController@change_pictures')->name('change_like_pictures');

});

