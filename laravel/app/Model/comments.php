<?php
namespace App\Model;
use  Illuminate\Database\Eloquent\Model;

class comments extends Model 
{
	protected $table = 'comments';

	public function likes_comments()
	{
		return $this->hasMany('App\Model\likes_comments');
	}
	public function comments()
	{
		return $this->hasMany('App\Model\comments');
	}
	public function under_comments()
	{
		return $this->belongs_to('App\Model\comments');
	}
	public function pictures()
	{
		return $this->belongs_to('App\Model\pictures');
	}
}