package algorithms
import breeze.linalg.{Transpose, argmax, max, sum, DenseMatrix => BDM, DenseVector => BDV}
import scala.collection.immutable.IndexedSeq
/**
  * Created by tonyp on 2016-04-25.
  */
class MaxRelevanceOverMinRedundancyFeatureSelection {
  def evaluate(H: BDM[Double], f: BDV[Double], num_feat: Int): BDV[Int] = {
    val mRMRSorting: BDV[Int] = BDV.zeros[Int](num_feat)
    val selected: BDV[Double] = BDV.zeros[Double](num_feat)

    mRMRSorting(0) = argmax(f.toArray)
    printf("THe first f:  " + f.map(x=> x.toString + ","))
    printf("the maximum argument is : " +argmax(f.toArray))
    selected(mRMRSorting(0)) = 1.0
    println("The selected vector is : " + selected.map(x=>x.toString + ","))

    println(" Inside MRMR")

    val tmp: IndexedSeq[(BDV[Double], Int)] =(0 until H.rows).map{
      i=> H(i,::).t
    }.zipWithIndex

    println("  Row partitioning done")

    for (kk<-1 until num_feat){
      val red: IndexedSeq[Double] = tmp.map { case (x: BDV[Double], y) =>
        //println( "Iteration : "+ kk)
        if (selected(y) == 1.0) {
          0.0
        }else {
          val ret = x.t * selected
          //println("\nThe result: " + ret + " y : " + y)
          ret
        }
      }
      val newf: BDV[Double] = f :/ BDV(red.toArray).map(_/(kk))
      val increment: BDV[Double] =selected.mapPairs{ (x,y) => if (y == 1.0) 0 else newf(x) }
      mRMRSorting(kk) = argmax(increment)
      selected(argmax(increment))=1.0


      println(" The criterion : " + increment.map(x=>x.toString+" , "))
      println("THe MRMR " + mRMRSorting.map(x => x.toString + ","))
      println("The sum is " + sum(selected))
    }
    mRMRSorting
  }
  def ev(H:Array[Array[Double]])={
    H.apply(0).apply(0)
  }
}
