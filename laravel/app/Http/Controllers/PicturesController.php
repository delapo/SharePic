<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use App\Model\pictures;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use App\Http\Controllers\Services;


class PicturesController extends Controller
{

    public function add(Request $request)
    {
        $class = new Services\new_picture();

        $class->create_pic($request);
    }


    public function delete(Request $request)
    {
        $id = Auth::user()->id;
        $verif = DB::table('pictures')->where('image', $request['image'])->first()->user_id;

        if ($id == $verif) {
            Storage::disk('public')->delete($request['image']);
            Pictures::where('image', $request['image'])->delete();

            return "deleted";
        }
        else
            return "you're not allowed to delete it";
    }


    public function changes()
    {
        dd('changespic');
    }

    public function show(Request $request)
    {
        $start = $request->route('start');
        $end = $request->route('end');
        $user_id = $request->route('user_id');

        $dis = DB::table('pictures')->where('user_id', $user_id)
        ->latest()
        ->offset($start)
        ->take($end - $start)
        ->get();

        //dd($dis);
        return $dis;
    }

    public function showall(Request $request)
    {
        $user_id = $request->route('user_id');
        $dis = DB::table('pictures')->where('user_id', $user_id)->get();

        return $dis;
    }

    public function findPicLocalisation(Request $request)
    {
        $id = $request->route('id');
        $dis = DB::table('pictures')->where('id', $id)->first()->localisation;

        return $dis;
    }

    public function findPicDate(Request $request)
    {
        $id = $request->route('id');
        $dis = DB::table('pictures')->where('id', $id)->first()->date;

        return $dis;
    }

    public function findPicFile(Request $request)
    {
        $id = $request->route('id');
        $dis = DB::table('pictures')->where('id', $id)->first()->image;


        return $dis;
    }

    public function findProfilFile(Request $request)
    {
        $id = $request->route('id');
        $dis = DB::table('users')->where('id', $id)->first()->profil_picture;

        return $dis;
    }

    public function Picoffollowers(Request $request)
    {

        $id = Auth::user()->id;
        $list = DB::table('user_has_users')->where('follower_id', $id)->get();
        $size = $list->count();

        $dis = null;
        for ($x = 0; $x < $size ; $x++) {

        $dis[$x] = DB::table('pictures')->where('user_id', $list[$x]->user_id)->get('image');
        }
        dd($dis);
        return $dis;
    }

    public function NbrPic(Request $request)
    {
        $id = $request->route('id');
        $list = DB::table('pictures')->where('user_id', $id)->get();
        $size = $list->count();
        $dis = null;

        return $size;
    }
}
