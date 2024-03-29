package pl.pwr.logger.client;

import java.util.ArrayList;

import gwt.canvas.client.Canvas;
//import java.math.*;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.KeyCodes;
import com.google.gwt.event.dom.client.KeyUpEvent;
import com.google.gwt.event.dom.client.KeyUpHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.DialogBox;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.HorizontalPanel;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.MouseListener;
import com.google.gwt.user.client.ui.RootPanel;
import com.google.gwt.user.client.ui.TextBox;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;


/**
 * Entry point classes define <code>onModuleLoad()</code>.
 */
public class GLogger implements EntryPoint
{
	
	
   /**
    * Create a remote service proxy to talk to the server-side Greeting service.
    */
   private final LoggingServiceAsync loggingService = GWT
         .create(LoggingService.class);

   /**
    * This is the entry point method.
    */
   public void onModuleLoad()
   {
	   final HorizontalPanel bPanel = new HorizontalPanel();
	   final DrawingArea drawingArea = new DrawingArea();
	   bPanel.setPixelSize(300, 50);
	   bPanel.setSpacing(5);
	   final VerticalPanel vPanel = new VerticalPanel();
	   final Button sendButton = new Button("Log in");
		final Button clearButton = new Button("Clear");
		final TextBox nameField = new TextBox();
		nameField.setText("User Name");
		
		

		// We can add style names to widgets
		sendButton.addStyleName("sendButton");
		
		bPanel.add(nameField);
		bPanel.add(sendButton);
		bPanel.add(clearButton);

		// Add the nameField and sendButton to the RootPanel
		// Use RootPanel.get() to get the entire body element
		RootPanel.get("nameFieldContainer").add(bPanel);
		//RootPanel.get("sendButtonContainer").add(sendButton);
		RootPanel.get("canvasContainer").add(drawingArea);
		RootPanel.get("MessageContainer").add(vPanel);

		// Focus the cursor on the name field when the app loads
		nameField.setFocus(true);
		nameField.selectAll();

		// Create the popup dialog box
		final DialogBox dialogBox = new DialogBox();
		dialogBox.setText("Remote Procedure Call");
		dialogBox.setAnimationEnabled(true);
		final Button closeButton = new Button("Close");
		// We can set the id of a widget by accessing its Element
		closeButton.getElement().setId("closeButton");
		final Label textToServerLabel = new Label();
		final HTML serverResponseLabel = new HTML();
		VerticalPanel dialogVPanel = new VerticalPanel();
		dialogVPanel.addStyleName("dialogVPanel");
		dialogVPanel.add(new HTML("<b>Sending name to the server:</b>"));
		dialogVPanel.add(textToServerLabel);
		dialogVPanel.add(new HTML("<br><b>Server replies:</b>"));
		dialogVPanel.add(serverResponseLabel);
		dialogVPanel.setHorizontalAlignment(VerticalPanel.ALIGN_RIGHT);
		dialogVPanel.add(closeButton);
		dialogBox.setWidget(dialogVPanel);

		// Add a handler to close the DialogBox
		closeButton.addClickHandler(new ClickHandler() {
			public void onClick(ClickEvent event) {
				dialogBox.hide();
				sendButton.setEnabled(true);
				sendButton.setFocus(true);
			}
		});

		// Create a handler for the sendButton and nameField
		class MyHandler implements ClickHandler, KeyUpHandler {
			/**
			 * Fired when the user clicks on the sendButton.
			 */
			public void onClick(ClickEvent event) {
				sendNameToServer();
				
			}

			/**
			 * Fired when the user types in the nameField.
			 */
			public void onKeyUp(KeyUpEvent event) {
				if (event.getNativeKeyCode() == KeyCodes.KEY_ENTER) {
					
					sendNameToServer();
				}
			}

			/**
			 * Send the name from the nameField to the server and wait for a response.
			 */
			private void sendNameToServer() {
				sendButton.setEnabled(false);
				String textToServer = nameField.getText();
				String dataToServer = drawingArea.getData();
				textToServerLabel.setText(textToServer);
				serverResponseLabel.setText("");
				loggingService.verify(textToServer, dataToServer,
						new AsyncCallback<Boolean>() {
							public void onFailure(Throwable caught) {
								// Show the RPC error message to the user
								dialogBox
										.setText("Remote Procedure Call - Failure");
								serverResponseLabel
										.addStyleName("serverResponseLabelError");
								serverResponseLabel.setHTML("server error");
								dialogBox.center();
								closeButton.setFocus(true);
							}

							public void onSuccess(Boolean result) {
								dialogBox.setText("Remote Procedure Call");
								serverResponseLabel
										.removeStyleName("serverResponseLabelError");
								if(result) serverResponseLabel.setHTML("Logged in.");
								else serverResponseLabel.setHTML("Invalid password or user name.");
								dialogBox.center();
								closeButton.setFocus(true);
							}
						});
			}
		}

		// Add a handler to send the name to the server
		MyHandler handler = new MyHandler();
		sendButton.addClickHandler(handler);
		nameField.addKeyUpHandler(handler);
		
		clearButton.addClickHandler(new ClickHandler() {
			
			@Override
			public void onClick(ClickEvent event) 
			{
				drawingArea.clear();
				
			}
		});
   }
  

}
