<?php

namespace App\Http\Controllers;

use Illuminate\Http\Request;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;

class CommentsController extends Controller
{

    public function add(request $request)
    {
        $class = new Services\new_comment();
        $class->create_comment($request);
    }


    public function delete(Request $request)
    {
        $id = Auth::user()->id;
        $verif = DB::table('comments')->where('id', $request['comment_id'])->first()->user_id;

        if ($id == $verif) {
            DB::table('comments')->where('id', $request['comment_id'])->delete();
            return "deleted";
        }
        else
            return "you're not allowed to delete it";
    }


    public function changes()
    {
        dd('changescomments');
    }
}
