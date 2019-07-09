<?php

namespace App;

use  Illuminate\Database\Eloquent\Model;
use Illuminate\Notifications\Notifiable;
use Laravel\Passport\HasApiTokens;
use Illuminate\Contracts\Auth\MustVerifyEmail;
use Illuminate\Foundation\Auth\User as Authenticatable;

class User extends Authenticatable
{
    use HasApiTokens, Notifiable;

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'name', 'email', 'password',
    ];

    /**
     * The attributes that should be hidden for arrays.
     *
     * @var array
     */
    protected $hidden = [
        'password', 'remember_token',
    ];

    /**
     * The attributes that should be cast to native types.
     *
     * @var array
     */
    protected $casts = [
        'email_verified_at' => 'datetime',
    ];

    public function likes_comments()
    {
        return $this->hasMany('App\Model\likes_comments');
    }
    public function likes_pictures()
    {
        return $this->hasMany('App\Model\likes_pictures');
    }
    public function pictures()
    {
        return $this->hasMany('App\Model\pictures');
    }
    public function comments()
    {
        return $this->hasMany('App\Model\comments');
    }
    public function user()
    {
        return $this->belongsToMany('App\User', 'user_has_users');
    }
    public function follower()
    {
        return $this->belongsToMany('App\User', 'user_has_users');
    }
    public function AauthAcessToken()
    {
        return $this->hasMany('\App\OauthAccessToken');
    }
}
