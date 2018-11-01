package model;


public class FoodEaten extends ItemDone{

    public void addDone(Item i){
        if (!this.getDone().contains(i)) {
            this.getDone().add(i);
            i.setCompleted(this);
        } else this.getDone().add(i);
    }

    public void removeDone(Item i){
        if (this.getDone().contains(i)){
            this.getDone().remove(i);
            i.setCompleted(new FoodEaten());
        }
    }

}
