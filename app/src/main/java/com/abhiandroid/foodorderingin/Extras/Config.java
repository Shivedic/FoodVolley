package com.abhiandroid.foodorderingin.Extras;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
/*
import com.abhiandroid.foodordering.Adapters.CartListAdapter;
import com.abhiandroid.foodordering.Fragments.ChoosePaymentMethod;
import com.abhiandroid.foodordering.Fragments.MyCartList;*/
import com.abhiandroid.foodorderingin.Activities.Login;
import com.abhiandroid.foodorderingin.Activities.MainActivity;
import com.abhiandroid.foodorderingin.Activities.SignUp;
import com.abhiandroid.foodorderingin.Adapter.CartListAdapter;
import com.abhiandroid.foodorderingin.Fragments.ChoosePaymentMethod;
import com.abhiandroid.foodorderingin.Fragments.MyCartList;
import com.abhiandroid.foodorderingin.Fragments.MyProfile;
import com.abhiandroid.foodorderingin.Fragments.PincodeList;
import com.abhiandroid.foodorderingin.MVP.CartistResponse;
import com.abhiandroid.foodorderingin.MVP.SignUpResponse;
import com.abhiandroid.foodorderingin.PaymentIntegrationMethods.OrderConfirmed;
import com.abhiandroid.foodorderingin.R;
import com.abhiandroid.foodorderingin.Retrofit.Api;
import com.abhiandroid.foodorderingin.Volley.Volley_Request;

import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static com.abhiandroid.foodorderingin.Activities.MainActivity.userId;

public class Config {
    // broadcast receiver intent filters
    public static final String REGISTRATION_COMPLETE = "registrationComplete";
    // id to handle the notification in the notification tray
    public static final String SHARED_PREF = "ah_firebase";
    static SweetAlertDialog pDialog = null;
public static Context mContext;

    public static void moveTo(Context context, Class targetClass) {
        Intent intent = new Intent(context, targetClass);
        context.startActivity(intent);
    }

    public static boolean validateEmail(EditText editText, Context context) {
        String email = editText.getText().toString().trim();

        if (email.isEmpty() || !isValidEmail(email)) {
            editText.setError(context.getString(R.string.err_msg_email));
            editText.requestFocus();
            return false;
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static void showCustomAlertDialog(Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
    }

    public static void showLoginCustomAlertDialog(final Context context, String title, String msg, int type) {
        SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);
        alertDialog.setCancelText("Login");
        alertDialog.setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Config.moveTo(context, Login.class);

            }
        });
        alertDialog.setConfirmText("Signup");
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                Config.moveTo(context, SignUp.class);

            }
        });
        if (msg.length() > 0)
            alertDialog.setContentText(msg);
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        btn1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

    }

    public static void showPincodeCustomAlertDialog(final Context context, String title, String msg, int type) {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        alertDialog.setConfirmText("Places We Deliver");
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
                ((MainActivity) context).loadFragment(new PincodeList(), true);

            }
        });
        if (msg.length() > 0) {
            alertDialog.setContentText(msg);
        }
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        btn1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

    }

    public static void showPincodeCustomAlertDialog1(final Context context, String title, String msg, int type) {
        final SweetAlertDialog alertDialog = new SweetAlertDialog(context, type);
        alertDialog.setTitleText(title);

        alertDialog.setConfirmText("Change Pincode");
        alertDialog.setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
            @Override
            public void onClick(SweetAlertDialog sweetAlertDialog) {
                alertDialog.dismissWithAnimation();
                ((MainActivity) context).loadFragment(new MyProfile(), true);

            }
        });
        if (msg.length() > 0) {
            alertDialog.setContentText(msg);
        }
        alertDialog.show();
        Button btn = (Button) alertDialog.findViewById(R.id.confirm_button);
        btn.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));
        Button btn1 = (Button) alertDialog.findViewById(R.id.cancel_button);
        btn1.setBackgroundColor(ContextCompat.getColor(context, R.color.colorPrimary));

    }

    public static void getCartList(final Context context, final boolean b) {
        if (b)
            MainActivity.progressBar.setVisibility(View.VISIBLE);
        MainActivity.cartCount.setVisibility(View.GONE);
        String req = "{\"res_id\":\"res007\",\"user_id\":\"" + MainActivity.userId + "\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(context, context.getString(R.string.mJSONURL_viewcart), "POST", "getCartList", req);
        /*
        Api.getClient().getCartList("res007", MainActivity.userId, new Callback<CartistResponse>() {
            @Override
            public void success(CartistResponse cartistResponse, Response response) {
                MainActivity.progressBar.setVisibility(View.GONE);
                try {
                    if (cartistResponse.getProducts().size() <= 0) {
                        MainActivity.cartCount.setVisibility(View.GONE);
                    } else {
                        MainActivity.cartCount.setText(cartistResponse.getProducts().size() + "");
                        if (!b) {
                            Log.d("equals", "equals");
                            MainActivity.cartCount.setVisibility(View.GONE);

                        } else {
                            MainActivity.cartCount.setVisibility(View.VISIBLE);

                        }
                    }
                } catch (Exception e) {
                    MainActivity.cartCount.setVisibility(View.GONE);

                }

            }

            @Override
            public void failure(RetrofitError error) {
                MainActivity.progressBar.setVisibility(View.GONE);
            }
        });
        */
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    public static void addOrder(final Context context, String transactionId, String paymentMode) {

        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        mContext = context;
        String req = "{\"res_id\":\"res007\",\"user_id\":\"" + "7" + "\",\"cart_id\":\"" + MyCartList.cartistResponseData.getCartid() + "\",\"address\":\"" + ChoosePaymentMethod.address.substring(0, ChoosePaymentMethod.address.length() - 10).trim() + "\",\"phone\":\"" + ChoosePaymentMethod.mobileNo + "\",\"paymentref\":\"" + transactionId + "\",\"paystatus\":\"" + "succeeded" + "\",\"total\":\"" + CartListAdapter.totalAmountPayable + "\",\"paymentmode\":\""+ paymentMode +"\"}";
        Volley_Request postRequest = new Volley_Request();
        postRequest.createRequest(context, context.getResources().getString(R.string.mJSONURL_addorders), "POST", "addOrder", req);

/*
        Api.getClient().addOrder("res007", MainActivity.userId,
                MyCartList.cartistResponseData.getCartid(),
                ChoosePaymentMethod.address,
                ChoosePaymentMethod.mobileNo,
                transactionId,
                "succeeded",
                CartListAdapter.totalAmountPayable,
                paymentMode,
                new Callback<SignUpResponse>() {
                    @Override
                    public void success(SignUpResponse signUpResponse, Response response) {
                        pDialog.dismiss();
                        Intent intent = new Intent(context, OrderConfirmed.class);
                        context.startActivity(intent);
                        ((Activity) context).finishAffinity();
                    }

                    @Override
                    public void failure(RetrofitError error) {
                         pDialog.dismiss();
                        ((Activity) context).finish();
                    }
                });
                */
    }

    public static void addOrederResponse(String response){
        try {
            pDialog.dismissWithAnimation();
            Log.d("myTag", "recieved response : " + response);
            Log.d("myTag", "last index of " + response.substring(response.lastIndexOf(">") + 1, response.length()));
            String responseString = response.substring(response.lastIndexOf(">")+ 1, response.length());
            JSONObject jObj = new JSONObject(responseString);
            if(jObj.getString("success").equals("true")) {
                Intent intent = new Intent(mContext, OrderConfirmed.class);
                mContext.startActivity(intent);
                ((Activity) mContext).finishAffinity();
            }
        } catch (Exception e ) {
            Log.d("myTag", "error in add order : " , e);
        }
    }
}