<?php
namespace App\Model;
use  Illuminate\Database\Eloquent\Model;

class likes_comments extends Model 
{
	protected $table = 'likes_comments';

	public function user()
	{
		return $this->belongs_to('App\User');
	}
	public function comments()
	{
		return $this->belongs_to('App\Model\comments');
	}
}
