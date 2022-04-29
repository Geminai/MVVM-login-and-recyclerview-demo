package com.dvl.sigma.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.dvl.sigma.data.models.responses.Response
import com.dvl.sigma.databinding.FragmentHomeBinding
import com.dvl.sigma.utils.GridSpacingItemDecoration
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class HomeFragment : Fragment() {

    private val TAG = "HomeFragment"

    lateinit var binding: FragmentHomeBinding

    @Inject
    lateinit var categoryAdapter: CategoryAdapter

    val viewModel: HomeViewModel by viewModels()

    lateinit var homeActivity: HomeActivity

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        binding = FragmentHomeBinding.inflate(inflater)

        homeActivity = activity as HomeActivity

        // setting grid layout manager to implement grid view.
        // in this method '2' represents number of columns to be displayed in grid view.
        val layoutManager = GridLayoutManager(context, 2)


        // at last set adapter to recycler view.
        binding.rvCategoryList.layoutManager = layoutManager
        binding.rvCategoryList.adapter = categoryAdapter

        binding.rvCategoryList.addItemDecoration(
            GridSpacingItemDecoration(
                2,
                40, true
            )
        )

        initCategory()

        // Inflate the layout for this fragment
        return binding.root
    }

    private fun initCategory() {
        homeActivity.showProgress()

        viewModel.categoryLiveData.observe(viewLifecycleOwner, Observer {

            when (it) {
                is Response.Loading -> {
                    Log.e(TAG, "Loading...")

                }
                is Response.Success -> {
                    Log.e(TAG, "Success...")
                    homeActivity.hideProgress()

                    val categoryList = it.data?.categoryData?.categoryList?.drop(0)

                    categoryAdapter.submitList(categoryList)
                }
                is Response.Error -> {
                    Toast.makeText(activity, it.errorMessage, Toast.LENGTH_SHORT).show()
                    homeActivity.hideProgress()
                    Log.e(TAG, "Error == ${it.errorMessage}")
                }
            }


        })
    }


}