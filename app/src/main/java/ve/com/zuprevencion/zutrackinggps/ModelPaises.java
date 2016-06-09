package ve.com.zuprevencion.zutrackinggps;

import android.graphics.Bitmap;
import android.text.Spanned;
import java.io.Serializable;

public class ModelPaises implements Serializable {
    private String nombre = null;
    private Bitmap bandera = null;
    private String codigo = null;

    /***********
     * Set Methods
     ******************/
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setBandera(Bitmap bandera) {
        this.bandera = bandera;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    /***********
     * Get Methods
     ****************/
    public String getNombre() {
        return this.nombre;
    }

    public Bitmap getBandera() {
        return this.bandera;
    }

    public String getCodigo() {
        return this.codigo;
    }
}