<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class LikesComments extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('likes_comments', function (Blueprint $table) {
            $table->increments('id')->unsigned();
            $table->integer('comment_id')->unsigned();
            $table->integer('user_id')->unsigned();
            $table->timestamps();

        });
        Schema::table('likes_comments', function (Blueprint $table) {
            $table->foreign('comment_id')->references('id')->on('comments');
            $table->foreign('user_id')->references('id')->on('users');
        });
    }

    /**
     * Reverse the migrations.
     *
     * @return void
     */
    public function down()
    {
        Schema::dropIfExists('likes_comments');
        //
    }
}
