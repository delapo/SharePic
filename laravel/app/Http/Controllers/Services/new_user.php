<?php

namespace App\Http\Controllers\Services;

use Illuminate\Support\Facades\Hash;
use App\Http\Controllers\Controller;
use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;
use App\User;
use Illuminate\Support\Facades\Validator;
use Illuminate\Support\Facades\Storage;

class new_user extends Controller
{
    public function create_user(Request $request)
    {
        $user = new User();

        $validator= Validator::make($request->all(), [
            'name' => 'required|min:3|max:50',
            'email' => 'email',
            'password' => 'min:4|required_with:password_confirmation|same:password_confirmation',
            'password_confirmation' => 'min:4'
        ]);

        if ($validator->fails())
        {
            $failed = $validator->failed();
            dd($failed);
        }


        $name_profil =  $request['email'];
        if($request['profil_picture']) {
		$path = Storage::disk('public')->putFile("photos/profil/" . $name_profil, $request['profil_picture'] );
	}
        $user->name = $request['name'];
        $user->password = Hash::make($request['password']);
        $user->password_confirmation = Hash::make($request['password_confirmation']);
        $user->lastname = $request['lastname'];
        $user->username = $request['username'];
        $user->email = $request['email'];
        $user->isconnect = $request['isconnect'];
        if($request['profil_picture']) {
		$user->profil_picture = $path;
        }
	$user->save();


        return "user create";
    }
}
