package com.jeongyoung.thisisrecyclerview

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jeongyoung.thisisrecyclerview.databinding.ActivityMainBinding
import com.jeongyoung.thisisrecyclerview.databinding.ItemRecyclerBinding
import java.text.SimpleDateFormat

class MainActivity : AppCompatActivity() {

    val binding by lazy {ActivityMainBinding.inflate(layoutInflater)}

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        //1.데이터 불러온다
        val data = loadData()
        //2.아답터 생성
        val customAdapter = CustomAdapter(data)
        //3.화면의 Recyclerview와 연결
        binding.recyclerView.adapter = customAdapter
        binding.recyclerView.layoutManager = LinearLayoutManager(this)
    }

    fun loadData(): MutableList<Memo> {
        val memoList = mutableListOf<Memo>()
        for (no in 1..100) {
            val title = "RecyclerView예제 $no"
            val date = System.currentTimeMillis()
            val author = "김철수"
            val memo = Memo(no, title, date,author)
            memoList.add(memo)
        }
        return memoList
    }
}
// 1.getItemCount()에서 전체 아이템 사이즈를 계산
// 2.onCreateViewHolder()에서 레이아웃을 몇개 만들지 계산
// 3.onBindViewHolder() 화면이거나 화면이 넘어갔을때 현재 리스트의 "위치"를 뷰홀더에 전달

// 4. viewholder에서는 CustomAdapter를 받고 화면에 출력
class CustomAdapter(val memoList: MutableList<Memo>) : RecyclerView.Adapter<CustomAdapter.viewholder>() {
//val이 생성자에 있으면 전역변수 처럼 사용

    inner class viewholder(val binding: ItemRecyclerBinding) : RecyclerView.ViewHolder(binding.root) {
        //click Listener => setMemo 함수 內 사용-> 스크롤 할때마다 onBindViewHolder()가 호출되어서 넘길때마다 리스너 발생
        //init 사용

        lateinit var  currentMemo:Memo
        init{
            binding.root.setOnClickListener {
                val title = binding.textTitle.text.toString()
                Toast.makeText(binding.root.context,"${title}이 click되었습니다,저자 ${currentMemo.author}",Toast.LENGTH_SHORT).show()
            }
        }
        //onBindViewHolder()를 통헤 몇번째 데이터를 꺼내서 넣을지 알수있다
        fun setMemo(memo: Memo) {
            currentMemo = memo //여기서 메모의 값을 받아온다(클릭 리스너)
            with(binding) {
                textNo.text = "${memo.no}"
                textTitle.text = "${memo.title}"

                val sdf = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
                val formattedDate = sdf.format(memo.timestamp)
                textDate.text = formattedDate
            }
        }
    }

    //parent Layout에 만들 줄마다의 뷰 홀더를 만드는것->layout관련
    //화면에 8개의 리스트를 정렬할수있다면 ->8번 호출 item의 사이즈에따라서
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomAdapter.viewholder {
        val binding = ItemRecyclerBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return viewholder(binding)
    }

    override fun onBindViewHolder(holder: CustomAdapter.viewholder, position: Int) {
        //1.데이터를 꺼내고
        val memo = memoList.get(position)
        //2.홀더에 데이터 전달
        holder.setMemo(memo)
    }

    override fun getItemCount() = memoList.size
}

