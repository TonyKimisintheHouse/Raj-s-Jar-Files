package edu.gatech.cs2340.rajsjarfiles.spacetrader.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;


import java.util.HashMap;

import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.player.Player;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.entity.universe.Planet;
import edu.gatech.cs2340.rajsjarfiles.spacetrader.model.Model;

class MapView extends View {

    private static final int SUN_RADIUS = 30;
    private static final int ORBIT_RATIO = 75;
    private static final int RATIO = 7;

    public MapView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onDraw(Canvas canvas) {

        super.onDraw(canvas);
        fillInBackground(canvas);
        fillInOrbit(canvas);
        fillInPlanet(canvas);
    }

    private void fillInBackground(Canvas c) {
        Paint paint = new Paint();
        paint.setColor(Color.BLACK);
        paint.setStyle(Paint.Style.FILL);
        c.drawPaint(paint);

        int x = c.getWidth()/2;
        int y = c.getHeight()/2;

        paint.setColor(Color.YELLOW);
        c.drawCircle(x, y, SUN_RADIUS * 2, paint);
    }

    private void fillInOrbit(Canvas c) {
        Planet[] planets = Model.getCurrent().getPlayer().getLocation().getSolarSystem().getPlanets();
        Paint paint = new Paint();
        paint.setColor(Color.GRAY);
        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(5);

        int x = c.getWidth()/2;
        int y = c.getHeight()/2;

        for (Planet planet: planets) {
            int or = planet.getOrbitRadius();
            c.drawCircle(x,y, (SUN_RADIUS * 2) + (or * ORBIT_RATIO), paint);
        }
    }

    private void fillInPlanet(Canvas c){

        Planet[] planets = Model.getCurrent().getPlayer().getLocation().getSolarSystem().getPlanets();
        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);

        for (Planet planet: planets) {
            float[] coord = getXAndY(planet, c);
            paint.setColor(Color.parseColor(planet.getHabitats().getColorHex()));
            c.drawCircle(coord[0],coord[1],planet.getRadius() * RATIO, paint);
        }

        for (Planet planet: planets) {

            String distanceLabel = "";
            Planet currentPlanet = Model.getCurrent().getPlayer().getLocation().getPlanet();
            if (planet.equals(currentPlanet)) {
                paint.setColor(Color.RED);
            } else {
                paint.setColor(Color.WHITE);
                distanceLabel = currentPlanet.getDist(planet) + "LY";
            }
            float[] coord = getXAndY(planet, c);
            paint.setTextSize(27);
            c.drawText(planet.getName() + " " + distanceLabel,  coord[0] + planet.getRadius() * RATIO , coord[1] - planet.getRadius() * RATIO, paint);
        }
    }

    private float[] getXAndY(Planet planet, Canvas c) {

        int x = c.getWidth()/2, y = c.getHeight()/2;
        int a = planet.getOrbitAngle();
        int h = SUN_RADIUS * 2 + planet.getOrbitRadius() * ORBIT_RATIO;

        int dx = 0, dy = 0;

        if (a >= 0 && a < 90) {
            dx = (int) (Math.sin(a) * h);
            dy = (int) (Math.cos(a) * h * -1);
        } else if (a >= 90 && a < 180) {
            a -= 90;
            dx = (int) (Math.cos(a) * h);
            dy = (int) (Math.sin(a) * h);
        } else if (a >= 180 && a < 270) {
            a -= 180;
            dx = (int) (Math.sin(a) * h * -1);
            dy = (int) (Math.cos(a) * h);
        } else {
            a -= 270;
            dx = (int) (Math.cos(a) * h * -1);
            dy = (int) (Math.sin(a) * h * -1);
        }

        x += dx;
        y += dy;

        float[] array = new float[2];
        array[0] = x;
        array[1] = y;

        return array;
    }
}