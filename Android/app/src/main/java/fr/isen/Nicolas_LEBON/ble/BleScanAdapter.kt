import android.bluetooth.le.ScanResult
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import fr.isen.Nicolas_LEBON.R
import fr.isen.Nicolas_LEBON.databinding.CellBleDeviceBinding

class BleScanAdapter(private val listBLE: MutableList<ScanResult>,
                     private val clickListener: (ScanResult) -> Unit) : RecyclerView.Adapter<BleScanAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = CellBleDeviceBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    class ViewHolder(binding: CellBleDeviceBinding) : RecyclerView.ViewHolder(binding.root) {
        val layout = binding.root
        val BleName: TextView = itemView.findViewById(R.id.blename)
        val BleAdresse: TextView = itemView.findViewById(R.id.bleadresse)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.BleName.text = listBLE[position].scanRecord?.deviceName.toString()
        holder.BleAdresse.text = listBLE[position].device.toString()
        holder.layout.setOnClickListener {
            clickListener(listBLE[position])
        }
        holder.BleName.text=listBLE[position].scanRecord?.deviceName.toString()
        holder.BleAdresse.text= "MAC : " +listBLE[position].device.address.toString()
        //holder.numberID.text =listBLE[position].scanRecord?.advertiseFlags.toString()

    }

    override fun getItemCount(): Int = listBLE.size
}