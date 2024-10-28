package com.udacity.asteroidradar.main

import android.content.Context
import android.net.ConnectivityManager
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.udacity.asteroidradar.R
import com.udacity.asteroidradar.databinding.FragmentMainBinding
import com.udacity.asteroidradar.model.Asteroid
import kotlinx.android.synthetic.main.fragment_main.*

class MainFragment : Fragment(), MyAdapter.ClickListener {
    lateinit var binding: FragmentMainBinding
    private val viewModel: MainViewModel by lazy {
        ViewModelProvider(this).get(MainViewModel::class.java)
    }
    private val adapter: MyAdapter = MyAdapter(this, ArrayList<Asteroid>())
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentMainBinding.inflate(inflater)
        binding.lifecycleOwner = this
        binding.asteroidRecycler.adapter = adapter
        viewModel.setAndInitialize(requireContext())
        binding.viewModel = viewModel

        setHasOptionsMenu(true)
        return binding.root

    }

    override fun onStart() {
        super.onStart()
        viewModel.loading.observe(viewLifecycleOwner){
            if (it)
                status_loading_wheel.visibility=View.VISIBLE
            else
                    status_loading_wheel.visibility=View.GONE
        }
        viewModel.asteroidList.observe(viewLifecycleOwner) {
           if((it==null)||(it.isEmpty()))
               Toast.makeText(requireContext(),"No Data Was Found",Toast.LENGTH_SHORT).show()
               adapter.list = it
               adapter.notifyDataSetChanged()
           //}
        }
        viewModel.pic.observe(viewLifecycleOwner) {
            if(it!=null)
                Glide.with(requireContext().applicationContext).load(it.url)
                .into(binding.activityMainImageOfTheDay)
        }
         val connection = (requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager).activeNetworkInfo?.isConnected
    viewModel.refesh(connection?:false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.main_overflow_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
         when (item.itemId) {
            R.id.show_rent_menu -> {
               viewModel.toggle()
                viewModel.loadToday()
            }
            R.id.show_all_menu -> {
                viewModel.toggle()
               viewModel.loadLive()
            }
            R.id.show_buy_menu -> {
                viewModel.toggle()
             viewModel.loadLive()
            }
        }


        return true
    }

    override fun onClick(position: Int) {
        findNavController().navigate(MainFragmentDirections.actionShowDetail(adapter.list[position]))
    }
}
