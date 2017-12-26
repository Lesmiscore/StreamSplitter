package com.nao20010128nao.StreamSplitter

import java.io.OutputStream

class StreamSplitter(
        private val size:Int,
        private val onFilled:(ByteArray,Int,Int)->Unit
):OutputStream(){
    var filling: OutputStream? = null

    init {
        next(null)
    }

    private fun next(overflow: SizeLimitedOutputStream.OverflowError?) {
        filling=SizeLimitedOutputStream(size,{ me, next->
            closeCurrent(me)
            next(next)
        },{
            closeCurrent(it)
        })
        if(overflow!=null){
            filling!!.write(overflow.buffer)
        }
    }

    private fun closeCurrent(me:SizeLimitedOutputStream){
        onFilled(me.buffer,0,me.size())
    }

    override fun write(p0: Int) {
        filling!!.write(p0)
    }

    override fun write(p0: ByteArray?) {
        filling!!.write(p0)
    }

    override fun write(p0: ByteArray?, p1: Int, p2: Int) {
        filling!!.write(p0, p1, p2)
    }

    override fun close() {
        filling!!.close()
    }
}