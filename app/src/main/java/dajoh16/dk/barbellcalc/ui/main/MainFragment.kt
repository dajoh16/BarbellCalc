package dajoh16.dk.barbellcalc.ui.main

import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.widget.addTextChangedListener
import dajoh16.dk.barbellcalc.R
import kotlinx.android.synthetic.main.main_fragment.*
import java.math.RoundingMode

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private lateinit var viewModel: MainViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.main_fragment, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        txtKgRes.keyListener = null
        percentageNumber.addTextChangedListener {
            onPercentageInputChanged()
        }
        maxKgNumber.addTextChangedListener {
            onMaxKgInputChanged()
        }
        radioGroup.setOnCheckedChangeListener { radioGroup, i ->
            onMaxKgInputChanged()
        }
        viewModel = ViewModelProviders.of(this).get(MainViewModel::class.java)
        // TODO: Use the ViewModel
    }

    fun onMaxKgInputChanged(){
        displayKg()
        displayPlates()
    }

    fun onPercentageInputChanged(){
        displayKg()
        displayPlates()
    }

    fun calculateKg(max: Double, percentage: Int): Double{
        var calcKg = max * (percentage/100.0)
        if(calcKg%1.25 == 0.0){
            return calcKg
        } else {
            return (Math.round(calcKg/2.50))*2.5;
        }
    }

    fun displayKg(){
        try {
            var max = maxKgNumber.text.toString().toDouble()
            var percentage = percentageNumber.text.toString().toInt()
            var calcKg = calculateKg(max,percentage)
            val kgRounded = "%.2f".format(calcKg)
            txtKgRes.setText(kgRounded)
        } catch (e: NumberFormatException ){
            txtKgRes.setText("NaN")
        }
    }


    fun displayPlates(){
        try {
            var max = maxKgNumber.text.toString().toDouble()
            var percentage = percentageNumber.text.toString().toInt()
            var calcKg = calculateKg(max,percentage)
            var plates = calculatePlates(calcKg)

            txtViewTwentyPlates.text = (plates[0].toString())
            txtViewFifteenPlates.text = (plates[1].toString())
            txtViewTenPlates.text = (plates[2].toString())
            txtViewFivePlates.text = (plates[3].toString())
            txtViewTwoFivePlates.text = (plates[4].toString())
            txtViewOneTwoFivePlates.text = (plates[5].toString())
        } catch (e: NumberFormatException ){
            txtKgRes.setText("NaN")
        }
    }

    fun calculatePlates(calcKg: Double): List<Int>{
        var plates = ArrayList<Int>(6)
        var remainder = 0.0
        if(radioGroup.checkedRadioButtonId == rdBtnLadyBarbell.id){
            remainder = calcKg-15.0
        } else if(radioGroup.checkedRadioButtonId == rdBtnMensBarbell.id){
            remainder = calcKg-20.0
        }

        var twenties = (remainder/20).toInt()
        var twentiesUsed = twenties-(twenties%2)
        plates.add(twentiesUsed)
        remainder = remainder- (20.0*twentiesUsed)

        var fifteen = (remainder/15).toInt()
        var fifteenUsed = fifteen-(fifteen%2)
        plates.add(fifteenUsed)
        remainder = remainder- (15.0*fifteenUsed)

        var tens = (remainder/10).toInt()
        var tensUsed = tens - (tens%2)
        plates.add(tensUsed)
        remainder = remainder- (10.0*tensUsed)

        var fives = (remainder/5).toInt()
        var fivesUsed = fives - (fives%2)
        plates.add(fivesUsed)
        remainder = remainder - (5.0 * fivesUsed)

        var twoFive = (remainder/2.5).toInt()
        var twoFiveUsed = twoFive - (twoFive%2)
        plates.add(twoFiveUsed)
        remainder = remainder - (2.50* twoFiveUsed)

        var oneTwoFive = (remainder/1.25).toInt()
        var oneTwoFiveUsed = oneTwoFive - (oneTwoFive%2)
        plates.add(oneTwoFiveUsed)

        return plates
    }


}