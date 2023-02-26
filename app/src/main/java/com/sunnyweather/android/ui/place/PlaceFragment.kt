package com.sunnyweather.android.ui.place

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.*
import androidx.lifecycle.Lifecycle.State.CREATED
import androidx.recyclerview.widget.LinearLayoutManager
import com.sunnyweather.android.databinding.FragmentPlaceBinding

class PlaceFragment:Fragment() {

    private var _binding: FragmentPlaceBinding? = null
    private val binding get() = _binding!!


    val viewModel by lazy { ViewModelProvider(this).get(PlaceViewModel::class.java) }
    private lateinit var adapter:PlaceAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentPlaceBinding.inflate(inflater, container, false)
        return binding.root

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    //自己写的
    override fun onAttach(context: Context) {
        super.onAttach(context)
        requireActivity().lifecycle.addObserver(object: LifecycleEventObserver {
            override fun onStateChanged(source: LifecycleOwner, event: Lifecycle.Event) {
                if(event.targetState == CREATED){
                    val layoutManager = LinearLayoutManager(activity)
                    binding.recyclerView.layoutManager = layoutManager
                    //？？？？
                    adapter = PlaceAdapter(this@PlaceFragment,viewModel.placeList)
                    binding.recyclerView.adapter = adapter
                    binding.searchPlaceEdit.addTextChangedListener{
                      val content = it.toString()
                      if(content.isNotEmpty()){
                          //改变了LiveData
                          viewModel.searchPlaces(content)
                      }else{
                          binding.recyclerView.visibility = View.GONE
                          binding.bgImageView.visibility = View.VISIBLE
                          viewModel.placeList.clear()
                          adapter.notifyDataSetChanged()
                      }
                    }
                    //则执行以下
                    viewModel.placeLiveData.observe(this@PlaceFragment, Observer {
                        val places = it.getOrNull()
                        if(places != null){
                            binding.recyclerView.visibility = View.VISIBLE
                            binding.bgImageView.visibility = View.GONE
                            viewModel.placeList.clear()
                            //获得的结果加入列表
                            viewModel.placeList.addAll(places)
                            //后续由adapter处理？？？
                            adapter.notifyDataSetChanged()
                        }else{
                            Toast.makeText(activity,"未能查询到任何地点",Toast.LENGTH_SHORT).show()
                            it.exceptionOrNull()?.printStackTrace()
                        }
                    })


                    lifecycle.removeObserver(this)
                }
            }

        })

    }


}