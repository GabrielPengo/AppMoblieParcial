package com.example.parcialapp

import android.content.DialogInterface
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import com.example.parcialapp.databinding.ActivityCadastroBinding

class CadastroActivity : AppCompatActivity() {
    private lateinit var binding: ActivityCadastroBinding
    private val viewModel: CadastroViewModel by viewModels()
    val usuariosBD = UsuariosBD.instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityCadastroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Restaure o estado, se houver
        if (savedInstanceState != null) {
            viewModel.nome = savedInstanceState.getString("nome") ?: ""
            viewModel.email = savedInstanceState.getString("email") ?: ""
            viewModel.password = savedInstanceState.getString("password") ?: ""
            viewModel.confPassword = savedInstanceState.getString("confPassword") ?: ""
        }

        // Preenche os campos com os dados do ViewModel
        binding.editTextNome.setText(viewModel.nome)
        binding.editTextEmail.setText(viewModel.email)
        binding.editTextSenha.setText(viewModel.password)
        binding.editTextConfSenha.setText(viewModel.confPassword)

        binding.regButton.setOnClickListener {
            // Salva os dados do usuário no ViewModel ao clicar no botão
            val nome = binding.editTextNome.text.toString()
            val email = binding.editTextEmail.text.toString()
            val password = binding.editTextSenha.text.toString()
            val confPassword = binding.editTextConfSenha.text.toString()

            if(nome.isEmpty() || nome.isBlank() || email.isEmpty() || email.isBlank() || password.isEmpty() || password.isBlank() || confPassword.isEmpty() || confPassword.isBlank()) {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Aviso")
                builder.setMessage("Preencha todos os campos!")
                builder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }

            else if(password == confPassword) {
                val novoUsuario = Usuario(nome, email, password)
                usuariosBD.adUsuario(novoUsuario)
                var clicouOk = false

                val builder = AlertDialog.Builder(this)
                builder.setTitle("Aviso")
                builder.setMessage("Cadastro feito com sucesso!")
                builder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()

            }
            else {
                val builder = AlertDialog.Builder(this)
                builder.setTitle("Aviso")
                builder.setMessage("Confirme a senha corretamente.")
                builder.setPositiveButton("OK") { dialog: DialogInterface, _: Int ->
                    dialog.dismiss()
                }

                val dialog = builder.create()
                dialog.show()
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        // Salva os dados do ViewModel no outState
        outState.putString("email", viewModel.email)
        outState.putString("password", viewModel.password)
    }
}

class CadastroViewModel : ViewModel() {
    var nome: String = ""
    var email: String = ""
    var password: String = ""
    var confPassword: String = ""
}
