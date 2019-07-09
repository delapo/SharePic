<?php

namespace App\Http\Controllers;

use App\User;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;

class UsersController extends Controller
{



    public function profilpic(Request $request)
    {

        $id = Auth::id();
        $path = Storage::disk('public')->putFile("photos/profil/" . $id, $request['profil_picture'] );

        DB::table('users')->where('id', $id)->update(array('profil_picture' => $path));

        return "pic added";
     }


    public function findId(Request $request)
    {
        $id = $request->route('id');
        $dis = DB::table('users')->where('id', $id)->first();
        //dd($dis);
        return $dis;
    }

    public function findUsername(Request $request)
    {
        $userName = $request->route('userName');
        $dis = DB::table('users')->where('username', $userName)->first();
        //dd($dis);
        return $dis;
    }

    public function Abo(Request $request)
    {
        $user_id = $request['user_id'];
        $follower_id = Auth::user()->id;

        DB::insert('insert into user_has_users (user_id, follower_id) values (?, ?)', [$user_id, $follower_id]);


        return "vous etes bien abonne";
    }

   public function Abonnementnbr(Request $request)
    {
 	$id = Auth::user()->id;
        $list = DB::table('user_has_users')->where('follower_id', $id)->get();
        //dd($list);
        return $list->count();
   }

   public function Abonbr(Request $request)
    {
 	$id = Auth::id();

        $list = DB::table('user_has_users')->where('user_id', $id)->get();

        return $list->count();
    }


    public function DesAbo(Request $request)
    {
        $user_id = $request['user_id'];
        $follower_id = Auth::user()->id;

        DB::table('user_has_users')->where('user_id', $user_id)->where('follower_id', $follower_id)->delete();


        return "vous etes bien desabonner";
    }

    public function GetAbonne(Request $request)
    {
        $id = $request->route('id');

        $list = DB::table('user_has_users')->where('user_id', $id)->get();
        return $list;
    }

    public function GetAbonnement(Request $request)
    {
        $id = $request->route('id');
        $list = DB::table('user_has_users')->where('follower_id', $id)->get();
        //dd($list);
        return $list;
    }

}
