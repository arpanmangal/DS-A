
public class ArrayDeque implements DequeInterface {

  private int sizeOfmyDeque = 1; // this is the size of array that we currently have
  private Object[] myDeque = new Object[sizeOfmyDeque]; // this is our array
  // head < tail indicates empty array
  private int head = -1;
  private int tail = 0;
  public void insertFirst(Object o){
    if (this.isEmpty())
    {
      // empty array
      // insert at tail and increase head
      myDeque[tail] = o;
      head++;
    }
    else if (head + 1 < sizeOfmyDeque)
    {
      // array has space to accomodate new element
      head++;
      myDeque[head] = o;
    }
    else
    {
      // we need a bigger array
      sizeOfmyDeque *= 2;
      Object[] tmp = new Object[sizeOfmyDeque];
      // fill the new array with empty cells appended to the original array
      /*  ----------     --------------------
         | |3|4|1|0| => | |3|4|1|0| | | | | |
         ----------     --------------------- 
      */
      for (int i = tail; i <= head; i++)
      {
        tmp[i] = myDeque[i];
      }
      // get rid of previous
      myDeque = tmp; // Garbage collector will look after old myDeque
      head++;
      myDeque[head] = o;
    }
  }
  
  public void insertLast(Object o){
    if (this.isEmpty())
    {
      // empty array
      // insert at tail and increase head
      myDeque[tail] = o;
      head++;
    } 
    else if (tail > 0)
    {
      // array has space to accomodate new element
      tail--;
      myDeque[tail] = o;
    }
    else
    {
      // we need a bigger array
      sizeOfmyDeque *= 2;
      Object[] tmp = new Object[sizeOfmyDeque];
      // fill the new array with empty cells prepended to the original array
      /*  ----------     --------------------
         |3|4|1|0| | => | | | | | |3|4|1|0| |
         ----------     --------------------
      */
        for (int i = tail; i <= head; i++)
        {
          tmp[i + sizeOfmyDeque / 2] = myDeque[i];
        }
      // get rid of previous
      myDeque = tmp; // Garbage collector will look after old myDeque
      tail = tail + sizeOfmyDeque / 2;
      head = head + sizeOfmyDeque / 2;
      tail--;
      myDeque[tail] = o;
    }
    
  }
  
  public Object removeFirst() throws EmptyDequeException{
    // check if a front object exists
    if (!this.isEmpty())
    {
      // we are ready to remove first element
      head--;
      // assuming we have to return the removed object
      return myDeque[head + 1];
    }
    else 
    {
      throw new EmptyDequeException("Deque is Empty");
    }
  }
  
  public Object removeLast() throws EmptyDequeException{
    // check if a last object exists
    if (!this.isEmpty())
    {
      // we are ready to remove the last element
      tail++;
      // assuming we have to return the removed object
      return myDeque[tail - 1];
    }
    else throw new EmptyDequeException("Deque is Empty");
  }
  public Object first() throws EmptyDequeException{
    // check if a front object exists
    if (!this.isEmpty())
    {
      // return the first element
      return myDeque[head];
    }
    else throw new EmptyDequeException("Deque is Empty");
  }
  
  public Object last() throws EmptyDequeException{
    // check if a last object exists
    if (!this.isEmpty())
    {
      // return the last element
      return myDeque[tail];
    }
    else throw new EmptyDequeException("Deque is Empty");
  }
  
  public int size(){
    return head - tail + 1; // if Deque is empty it will output 0
  }
  public boolean isEmpty(){
    return (tail > head); // empty when tail exceeds head
  }
  public String toString(){
    String s = "[";
    for (int i = tail; i < head; i++) // automatically checks for empty array as in that case tail > head
    {
      s += "" + myDeque[i].toString() + ", ";
    }
    if (!this.isEmpty()) // array is not empty
      s += myDeque[head]; // for pretty printing of last element
    s += "]";
    return s;
  }
  
  
  public static void main(String[] args){
    int  N = 10;
    DequeInterface myDeque = new ArrayDeque();
    for(int i = 0; i < N; i++) {
      myDeque.insertFirst(i);
      myDeque.insertLast(-1*i);
    }
   
    int size1 = myDeque.size();
    System.out.println("Size: " + size1);
    System.out.println(myDeque.toString());
    
    if(size1 != 2*N){
      System.err.println("Incorrect size of the queue.");
    }
    
    //Test first() operation
    try{
      int first = (int)myDeque.first();
      int size2 = myDeque.size(); //Should be same as size1
      if(size1 != size2) {
        System.err.println("Error. Size modified after first()");
      }
    }
    catch (EmptyDequeException e){
      System.out.println("Empty queue");
    }
    
    //Remove first N elements
    for(int i = 0; i < N; i++) {
      try{
        int first = (Integer)myDeque.removeFirst();
      }
      catch (EmptyDequeException e) {
        System.out.println("Cant remove from empty queue");
      }
      
    }
    
    
    int size3 = myDeque.size();
    System.out.println("Size: " + myDeque.size());
    System.out.println(myDeque.toString());
    
    if(size3 != N){
      System.err.println("Incorrect size of the queue.");
    }
    
    try{
      int last = (int)myDeque.last();
      int size4 = myDeque.size(); //Should be same as size3
      if(size3 != size4) {
        System.err.println("Error. Size modified after last()");
      }
    }
    catch (EmptyDequeException e){
      System.out.println("Empty queue");
    }
    
    //empty the queue  - test removeLast() operation as well
    while(!myDeque.isEmpty()){
        try{
          int last = (int)myDeque.removeLast();
        }
        catch (EmptyDequeException e) {
          System.out.println("Cant remove from empty queue");
        }
    }
    
    int size5 = myDeque.size();
    if(size5 != 0){
      System.err.println("Incorrect size of the queue.");
    }
   // System.out.println("Successfully terminated"); 
  }
  
}