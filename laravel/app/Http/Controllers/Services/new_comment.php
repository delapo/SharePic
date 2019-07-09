<?php

namespace App\Http\Controllers\Services;

use Illuminate\Support\Facades\Auth;
use App\Model\comments;
use Illuminate\Support\Facades\DB;
use Illuminate\Http\Request;
use App\Http\Controllers\Controller;

class new_comment extends Controller
{
    public function create_comment(Request $request)
    {
        $user_id = Auth::user()->id;

        $com = new comments();
        $com->user_id = $user_id;
        $com->picture_id = $request['picture_id'];
        $com->comments_from_id = $request['comments_from_id'];
        $com->date = $request['date'];
        $com->comment = $request['comment'];

        $com->save();
        //$dis = DB::table('comments');
        //dd($dis);

        return "comment is add";
    }
}
