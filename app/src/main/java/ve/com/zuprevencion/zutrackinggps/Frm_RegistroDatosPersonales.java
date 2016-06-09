package ve.com.zuprevencion.zutrackinggps;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.CountDownTimer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.InputFilter;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Locale;

public class Frm_RegistroDatosPersonales extends AppCompatActivity {

    private EditText txtNombres;
    private EditText txtApellidos;
    private EditText txtDireccion;
    private Spinner cboPaises;
    private EditText txtProvincia;
    private EditText txtTelefono1;
    private EditText txtTelefono2;
    CustomProgress dialogMessage = null;
    private String paisSeleccionado = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.frm_registro_datos_personales);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        txtNombres = (EditText)findViewById(R.id.txtNombres);
        txtNombres.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtApellidos = (EditText)findViewById(R.id.txtApellidos);
        txtApellidos.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtDireccion = (EditText)findViewById(R.id.txtDireccion);
        txtDireccion.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        ArrayList<ModelPaises> arrPaises = new ArrayList<>();

        cboPaises = (Spinner) findViewById(R.id.cboPaises);
        txtProvincia = (EditText)findViewById(R.id.txtProvincia);
        txtProvincia.setFilters(new InputFilter[] {new InputFilter.AllCaps()});

        txtTelefono1 = (EditText)findViewById(R.id.txtTelefono1);
        txtTelefono2 = (EditText)findViewById(R.id.txtTelefono2);
        AutoResizeTextView btnSiguiente = (AutoResizeTextView) findViewById(R.id.btnSiguiente);

        String[] locales = Locale.getISOCountries();

        ModelPaises pais;

        ArrayList<String> strPaises = new ArrayList<>();

        for (String countryCode : locales) {
            Locale obj = new Locale("", countryCode);
            String strPais = limpiarCadena(obj.getDisplayCountry(Locale.getDefault()).toUpperCase()) + "*" + obj.getCountry().toLowerCase();
            strPaises.add(strPais);
        }

        Collections.sort(strPaises,String.CASE_INSENSITIVE_ORDER);

        for(int i = 0;i<strPaises.size();i++){
            String[] infoPais = strPaises.get(i).split("\\*");
            String nombre = infoPais[0];
            String codigo = infoPais[1];

            if (arrPaises.isEmpty()){
                pais = new ModelPaises();
                pais.setNombre("");
                pais.setCodigo("");
                pais.setBandera(null);
                arrPaises.add(pais);
            }

            pais = new ModelPaises();
            pais.setNombre(nombre);
            pais.setCodigo(codigo.toUpperCase());
            pais.setBandera(BitmapFactory.decodeResource(getResources(),
                    getResources().getIdentifier("drawable/"+codigo,null,getApplicationContext().getPackageName())));
            arrPaises.add(pais);
        }

        Resources res = getResources();
        AdapterPaises adapterPaises = new AdapterPaises(Frm_RegistroDatosPersonales.this, R.layout.paises_rows, arrPaises,res);
        assert cboPaises != null;
        cboPaises.setAdapter(adapterPaises);

        cboPaises.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int pos = cboPaises.getSelectedItemPosition();

                if (pos > 0) {
                    paisSeleccionado = ((TextView)view.findViewById(R.id.lblNombrePais)).getText().toString();
                } else {
                    paisSeleccionado = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        assert btnSiguiente != null;

        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ocultarTeclado();

                String nombres = txtNombres.getText().toString().trim();
                String apellidos = txtApellidos.getText().toString().trim();
                String direccion = txtDireccion.getText().toString().trim();
                String provincia = txtProvincia.getText().toString().trim();
                String telefono1 = txtTelefono1.getText().toString().trim();
                String telefono2 = txtTelefono2.getText().toString().trim();

                if(nombres.length() == 0){
                    txtNombres.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txtNombres, InputMethodManager.SHOW_IMPLICIT);

                    String msj = getString(R.string.debe_ingresar_nombre);
                    mostrarMensaje(false,false,1,msj);
                    return;
                }

                if(apellidos.length() == 0){
                    txtApellidos.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txtApellidos, InputMethodManager.SHOW_IMPLICIT);

                    String msj = getString(R.string.debe_ingresar_apellido);
                    mostrarMensaje(false,false,1,msj);
                    return;
                }

                if(direccion.length() == 0){
                    txtDireccion.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txtDireccion, InputMethodManager.SHOW_IMPLICIT);

                    String msj = getString(R.string.debe_ingresar_direccion);
                    mostrarMensaje(false,false,1,msj);
                    return;
                }

                if(provincia.length() == 0){
                    txtProvincia.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txtProvincia, InputMethodManager.SHOW_IMPLICIT);

                    String msj = getString(R.string.debe_ingresar_provincia);
                    mostrarMensaje(false,false,1,msj);
                    return;
                }

                if(telefono1.length() == 0){
                    txtTelefono1.requestFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.showSoftInput(txtTelefono1, InputMethodManager.SHOW_IMPLICIT);

                    String msj = getString(R.string.debe_ingresar_telefono1);
                    mostrarMensaje(false,false,1,msj);
                    return;
                }

                if (cboPaises.getSelectedItemPosition() == 0){
                    String msj = getString(R.string.debe_seleccionar_pais);
                    mostrarMensaje(false,false,1,msj);
                    return;
                }

                Intent frm = new Intent(Frm_RegistroDatosPersonales.this, Frm_RegistroDatosAcceso.class);
                frm.putExtra("nombres",nombres);
                frm.putExtra("apellidos",apellidos);
                frm.putExtra("direccion",direccion);
                frm.putExtra("pais",paisSeleccionado);
                frm.putExtra("provincia",provincia);
                frm.putExtra("telefono1",telefono1);
                frm.putExtra("telefono2",telefono2);
                startActivity(frm);
            }
        });
    }

    public static String limpiarCadena(String input) {
        // Cadena de caracteres original a sustituir.
        String original = "áàäéèëíìïóòöúùuñÁÀÄÉÈËÍÌÏÓÒÖÚÙÜÑçÇ";
        // Cadena de caracteres ASCII que reemplazarán los originales.
        String ascii = "aaaeeeiiiooouuunAAAEEEIIIOOOUUUNcC";
        String output = input;
        for (int i=0; i<original.length(); i++) {
            // Reemplazamos los caracteres especiales.
            output = output.replace(original.charAt(i), ascii.charAt(i));
        }//for i
        return output;
    }

    private void ocultarTeclado() {
        // Check if no view has focus:
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void mostrarMensaje(Boolean esBienvenida, Boolean enProgreso,int icono, String msj){
        try{
            if(esBienvenida){
                if(dialogMessage != null) {
                    dialogMessage.dismiss();
                    dialogMessage = null;
                }

                dialogMessage = new CustomProgress(Frm_RegistroDatosPersonales.this,enProgreso,icono,msj);
                dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialogMessage.setCanceledOnTouchOutside(false);

                dialogMessage.show();

                CountDownTimer timer = new CountDownTimer(3000,1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                    }

                    @Override
                    public void onFinish() {
                        Intent frm = new Intent(Frm_RegistroDatosPersonales.this, Frm_Mapa.class);
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

                    dialogMessage = new CustomProgress(Frm_RegistroDatosPersonales.this,true,icono, msj);
                    dialogMessage.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    dialogMessage.setCanceledOnTouchOutside(true);
                    dialogMessage.show();
                }else{
                    if(dialogMessage != null) {
                        dialogMessage.dismiss();
                        dialogMessage = null;
                    }

                    dialogMessage = new CustomProgress(Frm_RegistroDatosPersonales.this,enProgreso,icono,msj);
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
}
