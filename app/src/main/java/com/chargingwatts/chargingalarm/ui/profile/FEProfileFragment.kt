package com.chargingwatts.chargingalarm.ui.profile


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingComponent
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.chargingwatts.chargingalarm.AppExecutors
import com.chargingwatts.chargingalarm.R
import com.chargingwatts.chargingalarm.binding.FragmentDataBindingComponent
import com.chargingwatts.chargingalarm.databinding.FragmentProfileBinding
import com.chargingwatts.chargingalarm.di.Injectable
import com.chargingwatts.chargingalarm.ui.common.RetryCallback
import com.chargingwatts.chargingalarm.util.autoCleared
import javax.inject.Inject

private const val EMPLOYEE_ID = "employee_id"

class FEProfileFragment : Fragment(), Injectable {
    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    @Inject
    lateinit var appExecutors: AppExecutors


    var binding by autoCleared<FragmentProfileBinding>()
    var dataBindingComponent: DataBindingComponent = FragmentDataBindingComponent(this)


    private lateinit var feProfileModel: FEProfileViewModel

    private var mEmployeeId: String? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            mEmployeeId = it.getString(EMPLOYEE_ID)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val dataBinding = DataBindingUtil.inflate<FragmentProfileBinding>(layoutInflater, R.layout.fragment_profile, container, false, dataBindingComponent)
        dataBinding.retryCallback = object : RetryCallback {
            override fun retry() {
                feProfileModel.retry()
            }
        }
//        dataBinding.onClickListener = object : View.OnClickListener{
//            override fun onClick(v: View?) {
//                findNavController().navigate(R.id.action_FEProfileFragment_to_secondFragment)
//            }
//        }
        dataBinding.btnNext.setOnClickListener {
            findNavController().navigate(R.id.action_FEProfileFragment_to_secondFragment)
        }
        binding = dataBinding
        binding.setLifecycleOwner(this)
        return dataBinding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        feProfileModel = ViewModelProviders.of(this, viewModelFactory)
                .get(FEProfileViewModel::class.java)
        feProfileModel.setEmployeeId("1")


        feProfileModel.feProfileData.observe(this, Observer { feResource ->
            binding.userDetail = feResource?.data
            binding.feProfileResource = feResource

        })

    }

    companion object {
        @JvmStatic
        fun newInstance(employeeId: String) =
                FEProfileFragment().apply {
                    arguments = Bundle().apply {
                        putString(EMPLOYEE_ID, employeeId)
                    }
                }
    }
}
