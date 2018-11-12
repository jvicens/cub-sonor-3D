package TUIO;




public class TuioInfo implements TuioListener {

        private boolean obj_active[] = {false, false, false};
        private float obj_position[][] = new float [3][2];
        private float obj_angle[] = new float[3];
        private float obj_orientation[][] = new float [3][2];


	public void addTuioObject(TuioObject tobj) {
		//System.out.println("add obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+") "+tobj.getX()+" "+tobj.getY()+" "+tobj.getAngle());
                if (tobj.getSymbolID() == 2) obj_active[0]=true;
                if (tobj.getSymbolID() == 3) obj_active[1]=true;
                if (tobj.getSymbolID() == 4) obj_active[2]=true;
                
        }

	public void updateTuioObject(TuioObject tobj) {
		//System.out.println("set obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+") "+tobj.getX()+" "+tobj.getY()+" "+tobj.getAngle()+" "+tobj.getMotionSpeed()+" "+tobj.getRotationSpeed()+" "+tobj.getMotionAccel()+" "+tobj.getRotationAccel());
                if (tobj.getSymbolID() == 2){
                    obj_position[0][0]= tobj.getX();
                    obj_position[0][1]= tobj.getY();
                    obj_orientation[0][0] = (float)Math.sin(tobj.getAngle());
                    obj_orientation[0][1] = (float)Math.cos(tobj.getAngle());
                    


                }
                if (tobj.getSymbolID() == 3){
                    obj_position[1][0]= tobj.getX();
                    obj_position[1][1]= tobj.getY();
                    obj_orientation[1][0] = (float)Math.sin(tobj.getAngle());
                    obj_orientation[1][1] = (float)Math.cos(tobj.getAngle());

                }
                if (tobj.getSymbolID() == 4){
                    obj_position[2][0]= tobj.getX();
                    obj_position[2][1]= tobj.getY();
                    obj_orientation[2][0] = (float)Math.sin(tobj.getAngle());
                    obj_orientation[2][1] = (float)Math.cos(tobj.getAngle());

                }
        }

	public void removeTuioObject(TuioObject tobj) {
		//System.out.println("del obj "+tobj.getSymbolID()+" ("+tobj.getSessionID()+")");
                if (tobj.getSymbolID() == 2) obj_active[0]=false;
                if (tobj.getSymbolID() == 3) obj_active[1]=false;
                if (tobj.getSymbolID() == 4) obj_active[2]=false;
	}

	public void addTuioCursor(TuioCursor tcur) {
		System.out.println("add cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+") "+tcur.getX()+" "+tcur.getY());
	}

	public void updateTuioCursor(TuioCursor tcur) {
		System.out.println("set cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+") "+tcur.getX()+" "+tcur.getY()+" "+tcur.getMotionSpeed()+" "+tcur.getMotionAccel());
	}

	public void removeTuioCursor(TuioCursor tcur) {
		System.out.println("del cur "+tcur.getCursorID()+" ("+tcur.getSessionID()+")");
	}

	public void refresh(TuioTime frameTime) {
		//System.out.println("refresh "+frameTime.getTotalMilliseconds());
	}

        public boolean[] getObjActive(){
            return (obj_active);
        }

        public float[][] getObjPosition(){
            return (obj_position);
        }
        public float[][] getObjOrientation(){
            return (obj_orientation);
        }

}
