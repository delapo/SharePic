<?php

namespace App\Http\Controllers;

use Illuminate\Support\Facades\Auth;

use App\Model\likes_comments;
use App\Model\likes_pictures;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\DB;
class LikeController extends Controller
{

    public function add_comments(Request $request)
    {
        $like = new likes_comments();

        $like->user_id = $request['user_id'];
        $like->comment_id = $request['comment_id'];

        $like->save();
        $dis = DB::table('likes_comments');

        return "comment like";
    }


    public function delete_comments(Request $request)
    {
        $id = Auth::user()->id;
        $verif = DB::table('likes_comments')->where('id', $request['like_id'])->first()->user_id;

        if ($id == $verif) {
            DB::table('likes_comments')->where('id', $request['like_id'])->delete();
            return "deleted";
        }
        else
            return "you're not allowed to dislike it";        }

    public function changes_comments()
    {
        dd('changeslike');
    }

    public function count_comments()
    {
        $nbOfLikes = DB::table('likes_comments')->count();
        //dd($nbOfLikes);
        return $nbOfLikes;
    }

    public function add_pictures(Request $request)
    {
        $like = new likes_pictures();

        $like->user_id = $request['user_id'];
        $like->pictures_id = $request['pictures_id'];

        $like->save();
        //$dis = DB::table('likes_comments');

        return "pic like";
    }


    public function delete_pictures(Request $request)
    {
        $id = Auth::user()->id;
        //$id = 2;

        $verif = DB::table('likes_pictures')->where('id', $request['like_id'])->first()->user_id;

        if ($id == $verif) {
            DB::table('likes_pictures')->where('id', $request['like_id'])->delete();
            return "deleted";
        }
        else
            return "you're not allowed to dislike it";
    }

    public function changes_pictures()
    {
        dd('changeslike');
    }
    public function count_pictures()
    {
        $nbOfLikes = DB::table('likes_pictures')->count();
        //dd($nbOfLikes);
        return $nbOfLikes;
    }

    public function nbrlikecom(Request $request)
    {
        $id = $request->route('id');
        $list = DB::table('likes_comments')->where('comment_id', $id)->get();
        $size = $list->count();

        return $size;
    }

    public function nbrlikepic(Request $request)
    {
        $id = $request->route('id');
        $list = DB::table('likes_pictures')->where('pictures_id', $id)->get();
        $size = $list->count();

        return $size;
    }
}
