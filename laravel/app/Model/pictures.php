<?php
namespace App\Model;
use  Illuminate\Database\Eloquent\Model;

class pictures extends Model 
{
	protected $table = 'pictures';
	public function user()
	{
		return $this->belongs_to('App\User');
	}
	public function like_pictures()
	{
		return $this->hasMany('App\Model\like_pictures');
	}
	public function comments()
	{
		return $this->hasMany('App\Model\comments');
	}
}