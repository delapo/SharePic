<?php

use Illuminate\Support\Facades\Schema;
use Illuminate\Database\Schema\Blueprint;
use Illuminate\Database\Migrations\Migration;

class Pictures extends Migration
{
    /**
     * Run the migrations.
     *
     * @return void
     */
    public function up()
    {
        Schema::create('pictures', function (Blueprint $table) {
            $table->increments('id')->unsigned();
            $table->integer('user_id')->unsigned();
            $table->string('image');
            $table->string('localisation')->nullable();
            $table->dateTime('date');
            $table->string('description')->nullable();
            $table->timestamps();
        });
        Schema::table('pictures', function (Blueprint $table) {
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
             Schema::dropIfExists('pictures');
   //
    }
}
