<?php

namespace App\Http\Controllers\Auth;

use Illuminate\Support\Facades\Storage;
use App\Model\comments;
use App\User;
use Illuminate\Database\Console\Migrations\FreshCommand;
use Illuminate\Support\Facades\DB;
use App\Http\Controllers\Services;
use App\Http\Controllers\Controller;
use Illuminate\Http\Request;
use Illuminate\Support\Facades\Hash;
use Illuminate\Support\Facades\Auth;
use Illuminate\Support\Facades\Validator;
use Illuminate\Foundation\Auth\RegistersUsers;
use Lcobucci\JWT\Parser;
class RegisterController extends Controller
{
    /*
    |--------------------------------------------------------------------------
    | Register Controller
    |--------------------------------------------------------------------------
    |
    | This controller handles the registration of new users as well as their
    | validation and creation. By default this controller uses a trait to
    | provide this functionality without requiring any additional code.
    |
    */

    use RegistersUsers;

    /**
     * Where to redirect users after registration.
     *
     * @var string
     */
    protected $redirectTo = '/home';

    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        $this->middleware('guest');
    }

    /**
     * Get a validator for an incoming registration request.
     *
     * @param  array  $data
     * @return \Illuminate\Contracts\Validation\Validator
     */
    protected function validator(array $data)
    {
        return Validator::make($data, [
            'name' => ['required', 'string', 'max:255'],
            'email' => ['required', 'string', 'email', 'max:255', 'unique:users'],
            'password' => ['required', 'string', 'min:8', 'confirmed'],
        ]);
    }

    /**
     * Create a new user instance after a valid registration.
     *
     * @param  array  $data
     * @return \App\User
     */
    protected function create(array $data)
    {
        return User::create([
            'name' => $data['name'],
            'email' => $data['email'],
            'password' => Hash::make($data['password']),
        ]);
    }

    public function profilpic(Request $request)
    {

	$id = Auth::user()->id;

	$path = Storage::disk('public')->putFile("photos/profil/" . $id, $request['profil_picture'] );

	$user = App\User::find($id);

	$user->profil_picture = $path;

	$user->save();

	return "pic added";
     }

    public function cre(Request $request)
    {
        $class = new Services\new_user();
       $class->create_user($request);
    }
    public function connect(Request $request)
    {
        if(Auth::attempt(['email' => request('email'), 'password' => request('password')]) )
        {
            $user = Auth::user();
            $tokenStr = $user->createToken('token')->accessToken;
            return $tokenStr;
        }
        else
            return 'Wrong parameters';
    }
    public function lister()
    {
        $dis = DB::table('users')->get();
        return $dis;
    }

    public function getauser(Request $request)
    {
        $id = $request->route('id');

        $dis = DB::table('users')->where('id', $id)->get();
        return $dis;
    }

    public function logout(Request $request)
    {
        $accessToken = $request->bearerToken();
        if($accessToken)
        {
            $id = (new Parser())->parse($accessToken)->getHeader('jti');

            DB::table('oauth_access_tokens')
                ->where('id', $id)
                ->update([
                    'revoked' => 1
                ]);
        }
        Auth::logout();
        return "logout";
    }

    public function usernbr()
    {
        $dis = DB::table('users');
        $size = $dis->count();

        return $size;
    }
}
