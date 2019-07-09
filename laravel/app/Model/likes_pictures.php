<?php
namespace App\Model;
use  Illuminate\Database\Eloquent\Model;

class likes_pictures extends Model 
{
	protected $table = 'likes_pictures';

	public function user()
	{
		return $this->belongs_to('App\User');
	}
	public function pictures()
	{
		return $this->belongs_to('App\Model\pictures');
	}
}
