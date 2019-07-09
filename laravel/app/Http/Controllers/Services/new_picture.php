<?php

namespace App\Http\Controllers\Services;

use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\DB;
use App\Model\pictures;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Storage;
use App\Http\Controllers\Controller;

class new_picture extends Controller
{
    public function create_pic(Request $request)
    {
        $name = Auth::user()->id;
        $path = Storage::disk('public')->putFile("photos/" . $name, $request['photo'] );

        $pic = new pictures();

        $pic->user_id = $name;
        $pic->image = $path;
        $pic->localisation = $request['localisation'];
        $pic->date = $request['date'];
        $pic->description = $request['description'];

        $pic->save();

        return "pic is add";
    }
}
