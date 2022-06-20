package com.sv.scanngo

import android.content.Context
import android.content.Intent
import android.media.Image
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.ColorRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.AlignmentLine
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.fragment.findNavController
import com.sv.scanngo.api.RetrofitInstance
import com.sv.scanngo.api.ScanNgoApi
import com.sv.scanngo.databinding.FragmentHomeBinding
import com.sv.scanngo.model.logout
import com.sv.scanngo.model.token
import retrofit2.Call
import retrofit2.Response


@ExperimentalMaterialApi
class Home : Fragment() {

    private var _binding:FragmentHomeBinding?=null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding= FragmentHomeBinding.inflate(inflater,container,false)
        binding.composeView.apply {
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                MaterialTheme {
                    HomeScreen()
                }
            }
        }
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        binding.qrbtn.setOnClickListener {
            findNavController().navigate(R.id.action_home2_to_qrscan)
        }
        binding.signAct.setOnClickListener {
            startActivity(Intent(context,Signup::class.java))
        }
        binding.logoutBtn.setOnClickListener {
            Logout()
        }
    }

    private fun Logout() {
        var tokenFile=activity?.getSharedPreferences("tokenfile", Context.MODE_PRIVATE)
        val default = resources.getString(R.string.app_name)
        val text = tokenFile?.getString("token",default).toString()
        var token = "Bearer_$text"
        val scanNgoApi = RetrofitInstance.buildService(ScanNgoApi::class.java)
        val requestCall=scanNgoApi.logout(token)
        requestCall.enqueue(object :retrofit2.Callback<logout>{
            override fun onResponse(call: Call<logout>, response: Response<logout>) {
                tokenFile!!.edit().putString("token","").apply()
                if(response.isSuccessful){
                   // Toast.makeText(activity, "Success", Toast.LENGTH_LONG).show()
                    startActivity(Intent(context,Signup::class.java))
                }
                else {
                    Toast.makeText(activity, "Log Out Failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<logout>, t: Throwable) {
                Toast.makeText(activity, "Could not connect to Server", Toast.LENGTH_SHORT).show()

            }
        })
    }

    ///Compose UI Implementation

    @Composable
    fun MainTheme(content: @Composable () -> Unit) {
        Surface(color = colorResource(id = R.color.grey)) {
                content()
            }

    }

    @Preview(showBackground = true,
    showSystemUi = true)
    @Composable
    fun preview(){
        MainTheme {
            HomeScreen()
        }

    }

    @Composable
    fun HomeScreen() {
        Box(Modifier.verticalScroll(rememberScrollState())) {
            Column(Modifier.padding(vertical = 25.dp)) {
                Content()
            }
        }
    }

    @Composable
    fun Content(){
        Column(Modifier.scrollable(state = rememberScrollState(), orientation = Orientation.Vertical)) {
            promotions()
            Spacer(modifier = Modifier.height(25.dp))
            Divider(color = Color.Black, thickness = 2.dp)
            Spacer(modifier = Modifier.height(25.dp))
            quickNavSection()
            Spacer(modifier = Modifier.height(25.dp))
            takeoutQuick()
            Spacer(modifier = Modifier.height(25.dp))
            oneStorePromo()
        }
    }



    @Composable
    fun promotions(){
        val listpos = rememberLazyListState(0)
        LazyRow(Modifier.height(180.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        state = listpos,
        contentPadding = PaddingValues(horizontal = 16.dp)){
            items(3) {
                    promotionItem(imagePainter = painterResource(id = R.drawable._995189))
            }
            listpos.isScrollInProgress
        }

    }
    @Composable
    fun promotionItem(
        imagePainter:Painter
    ){
        Card(onClick ={},
            Modifier.width(320.dp),
            shape = RoundedCornerShape(10.dp)) {
            Image(painter = imagePainter, contentDescription ="",
            modifier = Modifier.fillMaxHeight(),
            alignment = Alignment.CenterEnd,
            contentScale = ContentScale.Crop)
        }

    }

    @Composable
    private fun quickNavSection() {
            Box(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)) {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                   quickNavButtons(imagePainter = painterResource(id = R.drawable.eva_search_outline), title ="Search Product" )
                   quickNavButtons(imagePainter = painterResource(id = R.drawable.grommet_icons_map), title ="Store Locator" )
                   quickNavButtons(imagePainter = painterResource(id = R.drawable.ph_bag), title ="Takeout" )
                   quickNavButtons(imagePainter = painterResource(id = R.drawable.bx_bx_receipt), title ="Receipts" )
                   quickNavButtons(imagePainter = painterResource(id = R.drawable.bi_pin_map), title ="Shops Near Me" )
                }
            }
    }

    @Composable
    private fun quickNavButtons(
        imagePainter: Painter,
        title:String
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(onClick={},
                shape = RoundedCornerShape(10.dp),
                backgroundColor = Color.White,
                modifier = Modifier
                    .size(width = 55.dp, height = 55.dp)
                    //.clickable() { }
            ) {
                Image(
                    painter = imagePainter,
                    contentDescription = "",
                    alignment = Alignment.CenterEnd,
                    modifier = Modifier.padding(10.dp)
                )
            }
            Text(
                text = title,
                textAlign = TextAlign.Center,
                modifier = Modifier.width(50.dp),
                maxLines = 2,
                fontSize = 12.sp
            )
        }
    }

    @Composable
    private fun takeoutQuick(){
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp)) {
            Column {
                Row(modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = "Takeout",
                        fontWeight = FontWeight(500)
                    )
                    Text(text = "View more",
                    color = Color.Blue,
                    fontSize = 12.sp,
                    modifier = Modifier.clickable {  })
                }
                LazyRow(contentPadding = PaddingValues(horizontal = 10.dp),
                horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                    items(8) {
                        takeoutItem()
                    }
                }
            }
        }
    }

    @Composable
    fun takeoutItem(){
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Card(onClick={},
                modifier = Modifier.size(width = 85.dp, height = 85.dp),
                shape = RoundedCornerShape(10.dp)) {
                    Image(painter = painterResource(id = R.drawable.shop_add) , contentDescription = "",
                    modifier = Modifier
                        .fillMaxHeight(),
                    alignment = Alignment.CenterEnd,
                    contentScale = ContentScale.Crop)
            }
            Text(text = "D-Mart",
            modifier = Modifier.padding(vertical = 5.dp),
            textAlign = TextAlign.Center,
            fontSize = 12.sp)
        }
    }

    @Composable
    fun oneStorePromo(){
        Card(modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .height(250.dp),
        backgroundColor = Color.White,
        shape = RoundedCornerShape(10.dp)) {
        Column(modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Top) {
            Row(Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween) {
                Column() {
                    Text(
                        text = "2nd S.S Market Complex Ward No. 6, Hill Cart Rd",
                        modifier = Modifier
                            .width(200.dp)
                            .padding(15.dp),
                        fontSize = 12.sp
                    )
                    Card(modifier = Modifier
                        .size(width = 150.dp, height = 55.dp)
                        .padding(15.dp),
                        shape = RoundedCornerShape(5.dp),
                        backgroundColor = Color.Black) {
                        Row(Modifier.fillMaxSize()) {
                            Text(
                                text = "Locate on Map",
                                fontSize = 10.sp,
                                modifier = Modifier.align(Alignment.CenterVertically)
                                    .padding(horizontal = 10.dp),
                                color = Color.White
                            )
                            Image(painter = painterResource(id = R.drawable.map_pin), contentDescription ="",
                            modifier = Modifier.fillMaxHeight(),
                            contentScale = ContentScale.Crop)
                        }
                    }
                }
                Card(modifier = Modifier
                    .size(width = 120.dp, height = 120.dp)
                    .padding(15.dp),
                shape = RectangleShape,
                elevation = 0.dp) {
                    Image(painter = painterResource(id = R.drawable._565075_removebg_preview),
                        contentDescription ="",
                    modifier = Modifier.fillMaxHeight(),
                    alignment = Alignment.CenterEnd,
                    contentScale = ContentScale.Crop)
                }

            }

        }

        }
    }


}