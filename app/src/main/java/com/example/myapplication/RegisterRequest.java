package com.example.myapplication;

import com.android.volley.Response;

import java.util.HashMap;
import java.util.Map;
import com.android.volley.toolbox.StringRequest;

public class RegisterRequest extends StringRequest {

    private static final String REGISTER_REQUEST_URL="http://192.168.150.150/Register.php";
    private final Map<String, String> params;
    public RegisterRequest(String name, String username, int age, String password, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener,null);
        params=new HashMap<>();
        params.put("name", name);
        params.put("username", username);
        params.put("age", age+"");
        params.put("password", password);
}
    @Override
    public Map<String, String> getParams(){
        return params;
    }

    }
