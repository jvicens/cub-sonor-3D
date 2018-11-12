// java -cp ../cvp:./ ProvaAudio3D conf.parser


import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sound.sampled.AudioFormat;

import TUIO.*;
import Audio3D.*;

import utils.math.*;


public class MotorAudio3D {

    private static final boolean DEBUG = false;
    private static final boolean PRINT =true;
    private static final int BUFFER_SIZE_ms=5;
    private static final int HRTF_SIZE_ms=3;
   
	
    public static void main(String[] args) throws Exception {

        //Generic Data
        int sources_n = 2;
        float fs = 44100 ;
        boolean S1=true,S2=true,L=true;

        //Ray theory
        int images_max_order = 1;
        int images_n = 0;
        int dst_filter_order = 0;
        int abs_filter_order = 0;

        //TUIO
//        int port = 3333;
//        TuioInfo tuio = new TuioInfo();
//        TuioClient client = new TuioClient(port);
//        if (DEBUG) System.out.println("listening to TUIO messages at port "+port);
//        client.addTuioListener(tuio);
//        client.connect();

        int port = 3333;
        TuioGraf tuio = new TuioGraf();
        TuioClient client = new TuioClient(port);
        if (DEBUG) System.out.println("listening to TUIO messages at port "+port);
        client.addTuioListener(tuio.getTuioListener());
        client.connect();


        float obj_position[][]=new float[3][2];
        float obj_orientation[][]= new float [3][2];
        boolean obj_active[]=new boolean[3];
        int obj_percentil[]= new int[2];

        double Xsrc_0=0, Ysrc_0=0, Xlst=0, Ylst=0, Xsrc_1=0, Ysrc_1=0;

        // Source
        String tmp;
        String sufix="";
        int[] source_from = new int[sources_n];
        String[] source_filename = new String[sources_n];
        double[] volume = new double[sources_n];
        double[] source_x =  new double[sources_n];
        double[] source_y = new double[sources_n];
        double[] source_z = new double[sources_n];

        for (int i=0;i<sources_n;++i) {
            if (sources_n == 1) ;
            else sufix="_"+String.valueOf(i+1);
                //Source from
		tmp = "FromFile";
		source_from[i]=-1; 
		source_filename[i]="";
                if ( tmp.compareTo("FromFile") == 0 ) {
                    source_from[i]=Source.FromFile;
                    //Put the name of each Source
                    source_filename[0] = "/Users/Julian/Cubo/JAVA/Cub 6.3/CUB/data/sounds/44100/cavall_1.wav";
                    source_filename[1] = "/Users/Julian/Cubo/JAVA/Cub 6.3/CUB/data/sounds/44100/elefant_1.wav";
                } else {
                    if (DEBUG) System.out.println("source_from: only the \"FromFile\" option is supported");
                    System.exit(0);
                }
                for (int j=0;j<sources_n;++j) {
                    // Put the value of each source
                    volume[j] = 1;
                    
                    //Inicialització fonts
                    source_x[j] = 0;
                    source_y[j] = 0;
                    source_z[j] = 0;
                }
        }
			
        // Listener
        double listener_x = 0;
        double listener_y = 0;
        double listener_z = 0;
            
        int listener_id = 1003 ;
        //ToSpeaker (to headphones to realtime)
        tmp = "ToSpeaker";
        int listener_to=-1;
        String listener_filename="";
        int listener_file_duration= 0;
        if ( tmp.compareTo("ToFile") == 0 ) {
            listener_to=Listener.ToFile;
            //Name file out
            listener_filename = "/Users/Julian/Cubo/JAVA/Cub 6.3/CUB/data/sounds/prova3D.wav";
            listener_file_duration = 2;
        } else if ( tmp.compareTo("ToSpeaker") == 0) listener_to=Listener.ToSpeaker;
            else {
                if (DEBUG) System.out.println("listener_to: only the \"ToFile\" and \"ToSpeaker\" options are supported");
                System.exit(0);
            }
           		
        // Rectangular room
        double[] vertex1 = new double[3];
        double[] vertex2 = new double[3];
        // Put directly the TR60 number (true) or Sabine Calculate (false)
        boolean TR60_value=true;
            
        vertex1[0]=0;
        vertex1[1]=0;
        vertex1[2]=0;
            
        vertex2[0]=20;
        vertex2[1]=20;
        vertex2[2]=10;

        double[] A = new double [6];
        A[0]=A[1]=A[2]=A[3]=A[4]=A[5]=0.5;
        double TR60=0;
            
        if (TR60_value) {
            TR60 = 1.5;
            if (DEBUG) System.out.println("\tTR60: method discarded; explicit value taken");
        }else{
            tmp = "Sabine";
            if ( tmp.equals("Sabine") ) {
                double v = Math.abs(vertex2[0]-vertex1[0])*Math.abs(vertex2[1]-vertex1[1])*Math.abs(vertex2[2]-vertex1[2]);
                double a = (A[0]+A[1])*Math.abs(vertex2[1]-vertex1[1])*Math.abs(vertex2[2]-vertex1[2])+
								(A[2]+A[3])*Math.abs(vertex2[0]-vertex1[0])*Math.abs(vertex2[2]-vertex1[2])+
								(A[4]+A[5])*Math.abs(vertex2[0]-vertex1[0])*Math.abs(vertex2[1]-vertex1[1]);
                TR60=0.161*v/a;
            }
            if (DEBUG) System.out.println("\tTR60 ("+tmp+"): "+TR60);
        }
        //Distance delay
        double RtoD = 0.5;

        double max_dim;
        max_dim=Utils.rect2R(vertex2[0]-vertex1[0], vertex2[1]-vertex1[1], vertex2[2]-vertex1[2]);
			
        // Initialization
        if (DEBUG) System.out.println("Initializazing ...");

        // HRTF
        Hrtf hrtf = new Hrtf();	// Una classe Hrtf està associada a un listener
	hrtf.init(listener_id, fs, HRTF_SIZE_ms);

	// Source
	Source[] source = new Source[sources_n];
	for (int i=0;i<sources_n;++i) {
            source[i] = new Source();
            source[i].init(source_from[i], hrtf);
            source[i].setFileName(source_filename[i], fs, BUFFER_SIZE_ms);
            source[i].setPath (source_x[i], source_y[i], source_z[i]);
            source[i].nextPosition();
            source[i].setOrientation(0, 1, 0);
            source[i].setDstFilter(dst_filter_order);
	}
	if (sources_n > 1) 
            for (int i=0;i<sources_n-1;++i)
		if ( !source[i].getAudioFormat().matches(source[i+1].getAudioFormat())  ) { System.out.println("All the sources must have the same audio format"); System.exit(0); }
                fs = source[0].getAudioFormat().getSampleRate();;
               
        if (DEBUG) System.out.println("Sampling frequency: "+fs);
	
        // Listener, Forcem: 16 bits per sample, Stereo, 4 bytes per frame
	Listener listener = new Listener ();
	AudioFormat af = new AudioFormat (source[0].getAudioFormat().getEncoding(), source[0].getAudioFormat().getSampleRate(), 16, 2, 4, source[0].getAudioFormat().getFrameRate(), source[0].getAudioFormat().isBigEndian());
	listener.init(listener_to, af, BUFFER_SIZE_ms, listener_filename, listener_file_duration);
	listener.setPath (listener_x, listener_y, listener_z);
	listener.nextPosition();
	listener.setOrientation(0, 1, 0);
               
	// Room & image sources
	RectRoom rectRoom = new RectRoom();
	rectRoom.init(vertex1, vertex2, images_max_order, images_n);
	ImageSource[][] imageSources = new ImageSource[sources_n][];
	for (int i=0;i<sources_n;++i){
            imageSources[i]=rectRoom.getImageSources(source[i], A);
            for (int j=0;j<images_n;++j) {
                imageSources[i][j].setAbsFilter(abs_filter_order); // El coeficient d'absorció a baixes s'ha especificat a través de rectRoom
		imageSources[i][j].setDstFilter(dst_filter_order); // L'atenuació per distància a baixes es fixa a través de setAnglesAndDst
            }
	}
        // Schroeder reverberator
	ReverbSchroeder[] reverb=new ReverbSchroeder[sources_n];
	Vec[] out_rev = new Vec[sources_n];
        for (int i=0;i<sources_n;++i) {
            reverb[i] = new ReverbSchroeder();
            reverb[i].init(fs, TR60);
        }
        // Mixer
	Mixer[] mixer = new Mixer[sources_n];
	for (int i=0;i<sources_n;++i) mixer[i] = null;

        // Other variables
        Vec[][] out_hrtf_l = new Vec[sources_n][1+images_n];
        Vec[][] out_hrtf_r = new Vec[sources_n][1+images_n];
        int[][] itd = new int[sources_n][1+images_n];
        int[][] delay = new int[sources_n][1+images_n];
        // Execution
        if (DEBUG)System.out.println("Go!");
	int[] nBytesRead = new int[sources_n]; for (int i=0;i<sources_n;++i) nBytesRead[i]=0;
	boolean fi = false;
	double t = 0; 			 
        boolean ON = false; //S'activa i desactiva segons si hi ha el LISTENER o NO!
        //LOOP
        while ( fi == false ) {
            if (ON){
                for (int n=0;n<sources_n;++n) {
                    if ( nBytesRead[n] >= 0 ) {
                        if ( source[n].active() == true ) {
                            if (mixer[n] == null) {
                                mixer[n] = new Mixer();
                                mixer[n].init(1+images_n, (int) (Math.ceil(max_dim/Utils.c*fs)*(images_max_order+1)+BUFFER_SIZE_ms*fs/1000), RtoD);
                            }
                            nBytesRead[n]=source[n].read();
                            source[n].calculateParams(listener, fs);
                            itd[n][0]=source[n].getITD();
                            delay[n][0]=source[n].getDelay();
                            for (int i=0;i<images_n;++i) {
                                imageSources[n][i].calculateParams(listener, fs);
                                itd[n][i+1]=imageSources[n][i].getITD();
                                delay[n][i+1]=imageSources[n][i].getDelay();
                            }
                            source[n].dstFilter();
                            out_hrtf_l[n][0]=source[n].Filter_l();
                            out_hrtf_r[n][0]=source[n].Filter_r();
                            for (int i=0;i<images_n;++i) {
                                imageSources[n][i].absDstFilter();
                                out_hrtf_l[n][i+1]=imageSources[n][i].Filter_l();
                                out_hrtf_r[n][i+1]=imageSources[n][i].Filter_r();
                            }
                            out_rev[n]=reverb[n].reverb(source[n].getMono());
                            mixer[n].write(delay[n], itd[n], out_hrtf_l[n], out_hrtf_r[n], out_rev[n], out_rev[n]);
                            listener.mixStereo(mixer[n].read_l((int)(BUFFER_SIZE_ms*fs/1000)).mult(volume[n]), mixer[n].read_r((int)(BUFFER_SIZE_ms*fs/1000)).mult(volume[n]));
                        } else mixer[n] = null;
                    }
                }
                listener.writeStereo();
            }

            //Load TUIO data
            obj_active = tuio.getObjActive();
            obj_position = tuio.getObjPosition();
            obj_orientation = tuio.getObjOrientation();


            //If obj was IN -> Active sound
            //           OUT -> Desactive sound
           
            if (obj_active[0]){
                listener.nextRelativePosition(obj_position[0][0]*vertex2[0],((obj_position[0][1]*vertex2[1])+vertex2[1]),2);
                listener.setOrientation(obj_orientation[0][0], obj_orientation[0][1], 0);
                //System.out.println("Listener Orientation X: "+obj_orientation[0][0]+" , Y: "+obj_orientation[0][1]);
                //System.out.println("Listener Postion X: "+obj_position[0][0]*vertex2[0]+" , Y: "+(obj_position[0][1]*vertex2[1])+vertex2[1]);
                if (obj_active[1]){
                    source[0].setActive(true);
                    ON=true;
                    source[0].nextRelativePosition(obj_position[1][0]*vertex2[0],((obj_position[1][1]*vertex2[1])+vertex2[1]),2);
                    //source[0].setOrientation(obj_orientation[1][0], obj_orientation[1][1], 0);
                    //System.out.println( "Source Position X:"+obj_position[1][0]*vertex2[0]+ "Y:"+-((obj_position[1][1]*vertex2[1])+vertex2[1]));
                    //System.out.println("Font 1 X: "+obj_orientation[1][0]+" , Y: "+obj_orientation[1][1]);
                    //
                }else{
                    source[0].setActive(false); //OJO
                }
                if (obj_active[2]){
                    source[1].setActive(true);
                    ON=true;
                    source[1].nextRelativePosition(obj_position[2][0]*vertex2[0],((obj_position[2][1]*vertex2[1])+vertex2[1]),2);
                    //source[1].setOrientation(obj_orientation[2][0], obj_orientation[2][1], 0);
                    //System.out.println( "Y:"+-((-obj_position[2][1]*vertex2[1])+vertex2[1]));
                    //System.out.println("Font 2 X: "+obj_orientation[2][0]+" , Y: "+obj_orientation[2][1]);
                }else{
                    source[1].setActive(false); //OJO
                }
            }
            
            if (!source[0].available()){
                source[0].close();
                source[0].setFileName(source_filename[0], fs, BUFFER_SIZE_ms);
            }
            if (!source[1].available()){
                source[1].close();
                source[1].setFileName(source_filename[1], fs, BUFFER_SIZE_ms);
            }
            

                            
            
            
            

	}
        listener.drain();
        try {
            listener.close();
        } catch (Exception ex) {
            Logger.getLogger(MotorAudio3D.class.getName()).log(Level.SEVERE, null, ex);
        }
	for (int i=0;i<sources_n;++i) try {
            source[i].close();
        } catch (Exception ex) {
            Logger.getLogger(MotorAudio3D.class.getName()).log(Level.SEVERE, null, ex);
        }

    }
}


