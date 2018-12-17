/** this class represents a portion object - it can be either FOOD or DRINK.
 *  all nutritional values are saved as the amount is 100 grams of product.
 * @author Shaked Sapir
 * @since 2018-12-17*/
package Utils;


import java.util.Objects;

public class Portion {
    enum Type { FOOD , DRINK} ;

    private final Type type;
    private final String name;

    /** if food - in grams, if liquid - in milliliters **/
    private double amount;

    /** nutritional values **/
    private double calories_per_100_grams;
    private double proteins_per_100_grams;
    private double carbs_per_100_grams;
    private double fats_per_100_grams;


    public Portion(Portion.Type type , String name, double amount, double calories,
                   double proteins, double carbs, double fats) {
        this.type = type;
        this.name = name;
        this.amount = amount;
        this.calories_per_100_grams = calories;
        this.proteins_per_100_grams = proteins;
        this.carbs_per_100_grams = carbs;
        this.fats_per_100_grams = fats;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Portion portion = (Portion) o;
        return Double.compare(portion.amount, amount) == 0 &&
                Double.compare(portion.calories_per_100_grams, calories_per_100_grams) == 0 &&
                Double.compare(portion.proteins_per_100_grams, proteins_per_100_grams) == 0 &&
                Double.compare(portion.carbs_per_100_grams, carbs_per_100_grams) == 0 &&
                Double.compare(portion.fats_per_100_grams, fats_per_100_grams) == 0 &&
                type == portion.type &&
                Objects.equals(name, portion.name);
    }


    public double getAmount() {
        return amount;
    }

    public double getCalories_per_100_grams() {
        return calories_per_100_grams;
    }

    public double getCarbs_per_100_grams() {
        return carbs_per_100_grams;
    }

    public double getFats_per_100_grams() {
        return fats_per_100_grams;
    }

    public String getName() {
        return name;
    }

    public double getProteins_per_100_grams() {
        return proteins_per_100_grams;
    }

    public Type getType() {
        return type;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setCalories_per_100_grams(double calories_per_100_grams) {
        this.calories_per_100_grams = calories_per_100_grams;
    }

    public void setCarbs_per_100_grams(double carbs_per_100_grams) {
        this.carbs_per_100_grams = carbs_per_100_grams;
    }

    public void setFats_per_100_grams(double fats_per_100_grams) {
        this.fats_per_100_grams = fats_per_100_grams;
    }

    public void setProteins_per_100_grams(double proteins_per_100_grams) {
        this.proteins_per_100_grams = proteins_per_100_grams;
    }

    @Override
    public String toString() {
    	String units = this.type != Type.FOOD ? " ml" : " grams";
        return "Portion name: "+this.name+" , "+this.amount +units + "\n" +
                "Portion type: "+this.type+"\n" +
        		"----------------------------------\n"+
                "Nutritional Values per 100 grams:\n" +
                "Calories: "+this.calories_per_100_grams+"\n"+
                "Proteins: "+this.proteins_per_100_grams+"\n"+
                "Carbohydrates: "+this.carbs_per_100_grams+"\n"+
                "Fats: "+this.fats_per_100_grams;
    }
}
