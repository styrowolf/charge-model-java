package charge;

import vector.*;

public class App 
{
    // tested how the functions worked in main
    // this program is better off as a library actually
    // but nevermind, I needed the quickly test it so here's the main function
    public static void main( String[] args )
    {
        PointCharge q1 = new PointCharge(0.0, 0.3, 2.0e-6);
        PointCharge q2 = new PointCharge(0.0, 0.0, -4.0e-6);
        PointCharge q3 = new PointCharge(0.4, 0.0, 4.0e-6);
        Vector f_net = q3.net_force(new PointCharge[] {q1, q2} );
        System.out.println(f_net.magnitude);
        System.out.println(f_net.angle);
        System.out.println(f_net.angle / Math.PI * 180);
        
    }
}

class PointCharge {
    Coordinates c;
    Charge q;

    public PointCharge(double x, double y, double q) {
        this.c = new Coordinates(x, y);
        this.q = new Charge(q);
    }

    // whether two charges repel or attract each other
    public Interaction interaction(PointCharge other) {
        return this.q.interaction(other.q);
    }

    // for calculating the magnitude of the forces 
    // that the charges apply to each other
    private static double coulombs_law(double q1, double q2, double d) {
        double k = 8.99e9;
        return k * q1 * q2 / Math.pow(d, 2);
    }

    // calculates the force applied by the charge to another charge
    // uses Coulomb's law for magnitude calculation
    // and Vector.angle() for angle calculation
    public Vector force(PointCharge other) {
        double delta_x = other.c.x - this.c.x;
        double delta_y = other.c.y - this.c.y;

        double angle = Vector.angle(delta_x, delta_y);
        double distance = Math.hypot(delta_x, delta_y);

        double magnitude = coulombs_law(this.q.magnitude, other.q.magnitude, distance);
        if (this.interaction(other) == Interaction.Attract) {
            return new Vector(magnitude, angle);
        } else {
            return new Vector(magnitude, angle).recip();
        }
    }

    // calculates the net force on a charge 
    // by adding the forces applied by the other charges
    public Vector net_force(PointCharge[] others) {
        Vector f_net = new Vector(0.0, 0.0);
        for (int i = 0; i < others.length; i++) {
            f_net = Vector.add(f_net, this.force(others[i]));
        }
        return f_net;
    }
}

class Coordinates {
    double x;
    double y;

    public Coordinates(double x, double y) {
        this.x = x;
        this.y = y;
    }

}

class Charge {
    double magnitude;
    ChargeType sign;
    
    public Charge(double q) {
        this.magnitude = Math.abs(q);
        if (q < 0) {
            this.sign = ChargeType.Negative;
        } else {
            this.sign = ChargeType.Positive;
        }
    }

    public Interaction interaction(Charge other) {
        if (this.sign == other.sign) {
            return Interaction.Dispel;
        } else {
            return Interaction.Attract;
        }
    }
}

// booleans could've been used instead of these
// but for the sake of being more verbose I just used these
enum ChargeType {
    Positive,
    Negative,
}

enum Interaction {
    Attract,
    Dispel,
}