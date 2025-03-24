package mx.edu.itson.practicaautenticacionbastidasd

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth

class SignInActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_sign_in)
        auth = Firebase.auth

        val email: EditText = findViewById(R.id.etrEmail)
        val password: EditText = findViewById(R.id.etrPassword)
        val confirmPassword: EditText = findViewById(R.id.etrConfirmPassword)
        val errorTv: TextView = findViewById(R.id.tvrError)

        val btnRegister: Button = findViewById(R.id.btnRegister)
        val btnGoLogin: TextView = findViewById(R.id.btnGoLogin) // Este es el TextView clickeable

        errorTv.visibility = View.INVISIBLE

        btnRegister.setOnClickListener {
            if (email.text.toString().isEmpty() || password.text.toString().isEmpty() || confirmPassword.text.toString().isEmpty()) {
                errorTv.text = "Todos los campos son obligatorios"
                errorTv.visibility = View.VISIBLE
            } else if (password.text.toString() != confirmPassword.text.toString()) {
                errorTv.text = "Las contraseñas no coinciden"
                errorTv.visibility = View.VISIBLE
            } else {
                errorTv.visibility = View.INVISIBLE
                signIn(email.text.toString(), password.text.toString())
            }
        }

        btnGoLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java) //
            startActivity(intent)
        }
    }

    fun signIn(email: String, password: String) {
        Log.d("INFO", "email: $email password: $password")
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(this) { task ->
            if (task.isSuccessful) {
                Log.d("INFO", "createUserWithEmail:success")
                val user = auth.currentUser
                val intent = Intent(this, LoginActivity::class.java)
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
                startActivity(intent)
            } else {
                Log.w("ERROR", "createUserWithEmail:failure", task.exception)
                Toast.makeText(baseContext, "El registro falló", Toast.LENGTH_SHORT).show()
            }
        }
    }
}