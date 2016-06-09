package ve.com.zuprevencion.zutrackinggps;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import org.ksoap2.serialization.SoapObject;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Frm_Login extends Activity {

    private static final String TAG = "EJVH";
    SharedPreferences sPrefs;
    SharedPreferences.Editor sEditor;
    private static final String PROPERTY_LANGUAGE = "language";
    private ListView lstMenu;
    private DrawerLayout drawerLayout;
    String idiomaActual;
    Locale idiomaSistema;
    CustomProgress dialogMessage = null;
    Object response = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // ==============================================================================
        sPrefs = getSharedPreferences(Frm_Login.class.getSimpleName(), Context.MODE_PRIVATE);
        idiomaActual = sPrefs.getString(PROPERTY_LANGUAGE, "");
        idiomaSistema = getResources().getConfiguration().locale;

        if (!idiomaActual.equals("")) {
            if (!idiomaSistema.toString().contains(idiomaActual)) {
                String languageToLoad = idiomaActual;
                Locale locale = new Locale(languageToLoad);
                Locale.setDefault(locale);
                Configuration config = new Configuration();
                config.locale = locale;
                getBaseContext().getResources().updateConfiguration(config,
                        getBaseContext().getResources().getDisplayMetrics());
                restartApp();
            }
        } else {
            if (idiomaSistema.toString().contains("es")) {
                sEditor = sPrefs.edit();
                sEditor.putString(PROPERTY_LANGUAGE, "es");
                sEditor.apply();
                idiomaActual = "es";
            } else {
                sEditor = sPrefs.edit();
                sEditor.putString(PROPERTY_LANGUAGE, "en");
                sEditor.apply();
                idiomaActual = "en";
            }
        }

        setContentView(R.layout.frm_login);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        Button btnMenu = (Button) findViewById(R.id.btnMenu);
        final EditText txtEmail = (EditText) findViewById(R.id.txtEmail);
        final EditText txtPassword = (EditText) findViewById(R.id.txtPassword);
        AutoResizeTextView btnIniciarSesion = (AutoResizeTextView) findViewById(R.id.btnIniciarSesion);
        AutoResizeTextView btnRegistrar = (AutoResizeTextView) findViewById(R.id.btnRegistrar);
        lstMenu = (ListView) findViewById(R.id.lstMenuLogin);
        View menuHeader = getLayoutInflater().inflate(R.layout.lst_menuloginheader, null);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout_login);

        MenuItemsLogin menuItems[] = new MenuItemsLogin[]{
                new MenuItemsLogin(R.drawable.spainflag, getString(R.string.spanish)),
                new MenuItemsLogin(R.drawable.usaflag, getString(R.string.english))
        };

        MenuItemsArrayAdapterLogin adapter = new MenuItemsArrayAdapterLogin(this, R.layout.lst_menuitems, menuItems);
        lstMenu.addHeaderView(menuHeader);
        lstMenu.setAdapter(adapter);

        lstMenu.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 1:
                        if (!idiomaActual.equals("es")) {
                            String languageToLoad = "es";
                            Locale locale = new Locale(languageToLoad);
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;
                            getBaseContext().getResources().updateConfiguration(config,
                                    getBaseContext().getResources().getDisplayMetrics());

                            sEditor = sPrefs.edit();
                            sEditor.putString(PROPERTY_LANGUAGE, "es");
                            sEditor.apply();

                            restartApp();
                        }

                        break;
                    case 2:
                        if (!idiomaActual.equals("en")) {
                            String languageToLoad = "en";
                            Locale locale = new Locale(languageToLoad);
                            Locale.setDefault(locale);
                            Configuration config = new Configuration();
                            config.locale = locale;
                            getBaseContext().getResources().updateConfiguration(config,
                                    getBaseContext().getResources().getDisplayMetrics());

                            sEditor = sPrefs.edit();
                            sEditor.putString(PROPERTY_LANGUAGE, "en");
                            sEditor.apply();

                            restartApp();
                        }
                        break;
                    default:
                        break;
                }

                drawerLayout.closeDrawers();
            }
        });

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarTeclado();
                mostrarOcultarMenu();
            }
        });

        btnIniciarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = txtEmail.getText().toString().trim();
                String password = txtPassword.getText().toString().trim();

                if (email.length() == 0) {
                    String msj = getString(R.string.debe_ingresar_correo);
                    mostrarMensaje(false,false,1,msj);
                    return;
                }

                if (password.length() == 0) {
                    String msj = getString(R.string.debe_ingresar_clave);
                    mostrarMensaje(false,false,1,msj);
                    return;
                }

                if(!isEmailValid(email)){
                    String msj = getString(R.string.correo_invalido);
                    mostrarMensaje(false,false,1,msj);
                    return;
                }

                if(!isPasswordValid(password)){
                    String msj = getString(R.string.clave_invalida);
                    mostrarMensaje(false,false,1,msj);
                    return;
                }

                if(!estaConectado()){
                    String msj = getString(R.string.no_conectado_internet);
                    mostrarMensaje(false,false,1,msj);
                    return;
                }

                new AsyncLogin().execute(email, password);
            }
        });

        btnRegistrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent frm = new Intent(Frm_Login.this, Frm_RegistroDatosPersonales.class);
                startActivity(frm);
            }
        });
    }

    private boolean isEmailValid(String email) {
        String PATTERN_EMAIL = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        // Compiles the given regular expression into a pattern.
        Pattern pattern = Pattern.compile(PATTERN_EMAIL);

        // Match the given input against this pattern
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    private boolean isPasswordValid(String password) {
        return (password.length() >= 8 && password.length() <= 15);
    }

    private void ocultarTeclado() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void mostrarOcultarMenu() {
        if (drawerLayout.isDrawerOpen(lstMenu)) {
            drawerLayout.closeDrawers();
        } else {
            drawerLayout.openDrawer(lstMenu);
        }
    }

    protected Boolean estaConectado() {
        if (conectadoWifi()) {
            return true;
        } else {
            return conectadoRedMovil();
        }
    }

    protected Boolean conectadoWifi() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    protected Boolean conectadoRedMovil() {
        ConnectivityManager connectivity = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        if (connectivity != null) {
            NetworkInfo info = connectivity.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
            if (info != null) {
                if (info.isConnected()) {
                    return true;
                }
            }
        }
        return false;
    }

    private void restartApp() {
        Intent i = new Intent(Frm_Login.this, Frm_Login.class);
        i.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Frm_Login.this.startActivity(i);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_MENU){
            ocultarTeclado();
            mostrarOcultarMenu();
        }
        return super.onKeyDown(keyCode, event);
    }

    private void mostrarMensaje(Boolean esBienvenida, Boolean enProgreso,int icono, String msj){
        try{
            if(esBienvenida){
                if(dialogMessage != null) {
                    dialogMessage.dismiss();
                    dialogMessage = null;
                }

                dialogMessage = new CustomProgress(Frm_Login.this,enProgreso,icono,msj);
                dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogMessage.setCanceledOnTouchOutside(false);

                dialogMessage.show();

                CountDownTimer timer = new CountDownTimer(3000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        Intent frm = new Intent(Frm_Login.this, Frm_Mapa.class);
                        startActivity(frm);

                        if (dialogMessage != null){
                            dialogMessage.dismiss();
                            dialogMessage = null;
                        }
                    }
                };
                timer.start();
            }else{
                if(enProgreso){
                    if(dialogMessage != null){
                        dialogMessage.dismiss();
                        dialogMessage = null;
                    }

                    dialogMessage = new CustomProgress(Frm_Login.this,true,icono, msj);
                    dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogMessage.setCanceledOnTouchOutside(true);
                    dialogMessage.show();
                }else{
                    if(dialogMessage != null) {
                        dialogMessage.dismiss();
                        dialogMessage = null;
                    }

                    dialogMessage = new CustomProgress(Frm_Login.this,enProgreso,icono,msj);
                    dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogMessage.setCanceledOnTouchOutside(true);
                    dialogMessage.show();

                    CountDownTimer timer = new CountDownTimer(3000,1000) {
                        @Override
                        public void onTick(long millisUntilFinished) {
                        }

                        @Override
                        public void onFinish() {
                            if(dialogMessage != null){
                                dialogMessage.dismiss();
                                dialogMessage = null;
                            }
                        }
                    };
                    timer.start();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private class AsyncLogin extends AsyncTask<String,Integer,Integer> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Integer doInBackground(String... params) {
            publishProgress(0);

            ArrayList<Object> parametros = new ArrayList<>(4);
            parametros.add(0, "email*" + params[0]);
            parametros.add(1, "password*"+ params[1]);
            parametros.add(2, "loginCliente");

            GetDataWs ws = new GetDataWs();
            response = ws.getData(parametros);

            if (response.getClass() == SoapObject.class){
                //se obtiene los datos del web service
                SoapObject data = (SoapObject) response;

                if(data.getPropertyCount() > 0){
                    String responseKey = data.getProperty(0).toString();

                    Log.d(TAG + " responseKey", responseKey);

                    if(responseKey.equals("1")){ // usuario habilitado y validado
                        SoapObject infoCliente = (SoapObject) data.getProperty(1);

                        for(int i = 0;i<infoCliente.getPropertyCount();i++){
                            Log.d(TAG + " InfoCliente " + Integer.toString(i), infoCliente.getProperty(i).toString());
                        }
                    }else{
                        switch (responseKey){
                            case "0" : // usuario no validado
                                // redireccionar a la actividad de validacion de codigo
                                break;
                            case "2" : // usuario inhabilitado
                                break;
                            case "3" : // contraseña incorrecta
                                break;
                            case "4" : // usuario no registrado
                                break;
                            case "5" : // error de conexión
                                break;
                            default:
                                break;
                        }
                    }


                    //SoapObject clienteActual = (SoapObject) _cliente.getProperty(0);

                    /*
                    if(clienteActual.toString().trim().equals("anyType{}")){
                        publishProgress(1); // cedula/clave erronea
                        return 0;
                    }else{
                        idCliente = Integer.parseInt(clienteActual.getProperty(0).toString());
                        cedula = clienteActual.getProperty(1).toString();
                        cliente = clienteActual.getProperty(2).toString();

                        if(clienteActual.getProperty(9).toString().equals("0")){
                            publishProgress(2); // servicio suspendido
                            return 0;
                        }else{
                            if(clienteActual.getProperty(10).toString().equals("0")){
                                publishProgress(3); //sin vehiculos activos
                                return 0;
                            }else{
                                publishProgress(100); // abrir maps activity
                                return 1;
                            }
                        }
                    }*/
                }else{
                    publishProgress(1); // cedula/clave erronea
                    return 0;
                }
                return 0;
            }else{
                publishProgress(4); // error de conexion
                return 0;
            }
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
            String msj;
            /*
            switch (values[0]){
                case 0:
                    msj = getResources().getString(R.string.iniciandoSesion);
                    mostrarMensaje(false,true,0,msj);
                    break;
                case 1:
                    msj = getResources().getString(R.string.cedulaClaveIncorrecta);
                    mostrarMensaje(false,false,1,msj);
                    break;
                case 2:
                    msj = getResources().getString(R.string.estimadoCliente) + "\n" +
                            cliente + "\n" +
                            getResources().getString(R.string.servicioSuspendido);
                    mostrarMensaje(false,false,1,msj);
                    break;
                case 3:
                    msj = getResources().getString(R.string.estimadoCliente) + "\n" +
                            cliente + "\n" +
                            getResources().getString(R.string.sinVehiculosActivos);
                    mostrarMensaje(false,false,1,msj);
                    break;
                case 4:
                    msj = getResources().getString(R.string.errorConexion);
                    mostrarMensaje(false,false,2,msj);
                    break;
                case 100:
                    if(sPrefs == null){
                        sPrefs = getApplicationContext().getSharedPreferences(LoginActivity.class.getSimpleName(),Context.MODE_PRIVATE);
                    }

                    sEditor = sPrefs.edit();
                    sEditor.putInt(PROPERTY_USER_ID, idCliente);
                    sEditor.putString(PROPERTY_USER, cedula);
                    sEditor.putString(PROPERTY_NAME, cliente);
                    sEditor.putInt(PROPERTY_LOCKED, 0);
                    sEditor.putLong(PROPERTY_LAST_INTERACTION, System.currentTimeMillis());
                    sEditor.apply();

                    //se borra el contenido de los editText
                    txtPassword.setText(null);
                    txtCedula.setText(null);
                    txtCedula.requestFocus();
                    break;
            }
            */
        }

        @Override
        protected void onPostExecute(Integer s) {
            super.onPostExecute(s);

            if(s == 1){
                //String msj = getResources().getString(R.string.bienvenidoCliente) + "\n" + cliente;
                //mostrarMensaje(true, false, 0, msj);
            }
        }
    }
}

