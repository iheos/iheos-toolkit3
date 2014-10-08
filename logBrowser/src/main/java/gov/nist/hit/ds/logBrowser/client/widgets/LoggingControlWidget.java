package gov.nist.hit.ds.logBrowser.client.widgets;

import com.google.gwt.canvas.client.Canvas;
import com.google.gwt.canvas.dom.client.Context2d;
import com.google.gwt.canvas.dom.client.CssColor;
import com.google.gwt.event.shared.SimpleEventBus;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class LoggingControlWidget extends Composite {


    private SimpleEventBus eventBus;
    private String caption;



    // mouse positions relative to canvas
    int mouseX, mouseY;



    final CssColor redrawColor = CssColor.make("rgba(255,255,255,0.6)");
    Context2d context;
    Context2d backBufferContext;
    Canvas canvas;


    double centerX = 200;
    double centerY = 200;
    double radius = 10;

    /**
     *
     * @param caption
     */
   public LoggingControlWidget(Canvas canvas, SimpleEventBus eventBus, String caption) {
       this.canvas = canvas;
       this.eventBus = eventBus;
       this.caption = caption;

     // All composites must call initWidget() in their constructors.
     initWidget(initCanvas());

   }







    private Widget initCanvas(){


//        RootPanel.get(canvasHolderId).add(canvas);


        context = canvas.getContext2d();

        context.beginPath();
        context.setFillStyle(CssColor.make("gray")); //
//        FillStrokeStyle fillStrokeStyle = new




        context.arc(centerX, centerY, radius, 0, Math.PI * 2.0,  true );


        context.stroke();
//        context.fill();
        context.closePath();


        return canvas;
    }

    public void update(double mouseX, double mouseY) {


        context = canvas.getContext2d();

        context.beginPath();

        context.setFillStyle(CssColor.make("white")); //
            context.fillRect(0,0,300,50);
            context.fill();
        context.closePath();

        context.beginPath();
        context.setFillStyle(CssColor.make("black")); //
        context.fillText("x:" + mouseX + ",y:" + mouseY, 10, 10); 
        double relativeX = Math.abs(mouseX-centerX);
        double relativeY = Math.abs(mouseY-centerY);



        context.fillText("distance from circle center:" + relativeX  + ", " + relativeY, 10,20);
        double tan = Math.atan(relativeY / relativeX);

        context.fillText("deg: " + Math.toDegrees(tan) + " arctan: " + tan,10,30);
        double newY = radius * (Math.sin(tan));
        double newX = radius * (Math.cos(tan));
        context.fillText("newX: " +  newX + " newY: " + newY,10,40);


        // Q1
         if (mouseX>=centerX && mouseY<=centerY) {
             newX += centerX;
             newY = (centerY-newY);
         } else if (mouseX <= centerX && mouseY <= centerY) {

          // Q2
                newX = centerX-newX;
                newY = centerY-newY;

        } else if (mouseX <= centerX && mouseY >= centerY) {
          // Q3

             newX = centerX-newX;
             newY = centerY+newY;


         } else if (mouseX >= centerX && mouseY >= centerY) {
             // Q4

             newX += centerX;
             newY += centerY;
         }

        context.fillText("newC: " + (newX) + " newY: " + (newY),10,50);


        context.fillText("+",newX,newY);
        context.closePath();



    }

    public void draw(Context2d context) {

    }
 
}
