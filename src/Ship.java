public class Ship {


    private int shipSize;

    public Ship(int size) {
        this.shipSize = size;
    }


    public int getShipSize() {
        return shipSize;
    }



    @Override
    public boolean equals(Object o){
        if (o == null || getClass() != o.getClass()){
            return false;
        }
        return(((Ship) o).getShipSize() == this.shipSize);
    }

    @Override // found on https://stackoverflow.com/questions/2624192/good-hash-function-for-strings
    public int hashCode(){
        Integer x = this.shipSize;
        int hash = 31;


        hash = hash*157 + x;

        return hash;
    }

    @Override
    public String toString(){
        return "[" +shipSize + "]";
    }


}

