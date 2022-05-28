package com.example.firebasechatappwithkotlin


import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import com.example.firebasechatappwithkotlin.databinding.ActivityMainBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuth.AuthStateListener
import com.google.firebase.database.*


open class MainActivity : AppCompatActivity() {

    lateinit var binding: ActivityMainBinding
    private lateinit var auth: FirebaseAuth
    lateinit var authStateListener: AuthStateListener
    lateinit var dbRef: DatabaseReference
    lateinit var userList:ArrayList<Data>



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        userList = arrayListOf<Data>()



//        val user = Firebase.auth.currentUser
//        user?.let {
//            // Name
//            val name = user.displayName
//
//            binding.titleText.text = "hello: $name chat with other users..."
//        }


//        val user = auth.currentUser
//        val userid = user!!.uid
//
//        dbRef = FirebaseDatabase.getInstance().getReference("Users");
//        dbRef.child(userid).addListenerForSingleValueEvent(object: ValueEventListener{
//
//            override fun onDataChange(snapshot: DataSnapshot) {
//
//                val user: String = snapshot.child("UserName").value.toString()
//                binding.text.text = "hello: $user \n chat with the other users..."
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                throw  error.toException()
//            }
//
//        })


        // get loggedIn users
        dbRef = FirebaseDatabase.getInstance().getReference("Users")
        dbRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {

                userList.clear()
                if (snapshot.exists()) {
                    for (ds in snapshot.children) {
                        val name = ds.getValue(Data::class.java)
                        if (auth.currentUser?.uid != name?.uid) {
                            userList.add(name!!)
                        }
                    }
                    binding.recyclerView.adapter = RecyclerAdapter(this@MainActivity,userList)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                throw  error.toException()
            }
        })



        authStateListener = AuthStateListener { firebaseAuth ->

            val firebaseUser = firebaseAuth.currentUser
            if (firebaseUser == null) {
                val intent = Intent(this@MainActivity, Login::class.java)
                startActivity(intent)
                finish()
            }
        }



    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_item,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        if (item.itemId == R.id.logout){
            auth.signOut()
            startActivity(Intent(this@MainActivity, Login::class.java))
            finish()
        }
        return true
    }

    override fun onStart() {
        super.onStart()
        auth.addAuthStateListener(authStateListener)
    }
}
