package ru0xdc.rtklib;

import java.util.Arrays;
import java.util.Locale;

import javax.annotation.Nullable;

import proguard.annotation.Keep;


public class RtkCommon {

    /**
     * convert satellite number to satellite id
     * @param satNo satellite number
     * @return satellite id (Gnn,Rnn,Enn,Jnn,Cnn or nnn)
     */
    public static native String getSatId(int satNo);


    /**
     * compute DOP (dilution of precision)
     * @param azel  satellite azimuth/elevation angle (rad)
     * @param ns    number of satellites
     * @param dst   DOPs {GDOP,PDOP,HDOP,VDOP}
     */
    static void dops(final double[] azel, int ns, Dops dst) {
        dops(azel, ns, 0.0, dst);
    }

    /**
     * compute DOP (dilution of precision)
     * @param azel  satellite azimuth/elevation angle (rad)
     * @param ns    number of satellites
     * @param elmin elevation cutoff angle (rad)
     * @param dst   DOPs {GDOP,PDOP,HDOP,VDOP}
     */
    static native void dops(final double[] azel, int ns, double elmin, Dops dst);


    /**
     * get geoid height from geoid model
     * @param lat geodetic position lat (rad)
     * @param lon geodetic position lon (rad)
     * @return geoid height (m) (0.0:error)
     */
    public static native double geoidh(double lat, double lon);


    /**
     * convert degree to degree-minute-second
     * @param deg degree
     * @param dms degree-minute-second {deg,min,sec}
     */
    static native void _deg2dms(double deg, double dms[]);

    /**
     * euclid norm of vector
     * @param a vector a (n x 1)
     * @return || a ||
     */
    public static native double norm(double a[]);

    /**
     * Transform ecef position to geodetic position
     * @param x I ecef position (m)
     * @param y I ecef position (m)
     * @param z I ecef position (m)
     * @param dst O geodetic position {lat,lon,h} (rad,m). WGS84, ellipsoidal height
     */
    static native void _ecef2pos(double x, double y, double z, double dst[]);

    /**
     * Transform ecef position to geodetic position
     * @param x ecef position X (m)
     * @param y ecef position Y (m)
     * @param z ecef position Z (m)
     * @param dst geodetic position {lat,lon,h} (rad,m). WGS84, ellipsoidal height
     * @return dst
     */
    public static Position3d ecef2pos(double x, double y, double z, Position3d dst) {
        if (dst == null) {
            dst = new Position3d();
        }
        _ecef2pos(x, y, z, dst.mPos);
        return dst;
    }

    /**
     * Transform ecef position to geodetic position
     * @param x ecef position X (m)
     * @param y ecef position Y (m)
     * @param z ecef position Z (m)
     * @return dst geodetic position {lat,lon,h} (rad,m). WGS84, ellipsoidal height
     */
    public static Position3d ecef2pos(double x, double y, double z) {
        return ecef2pos(x, y, z, null);
    }

    /**
     * Transform ecef position to geodetic position
     * @param ecefPos ECEF position
     * @return geodetic position {lat,lon,h} (rad,m). WGS84, ellipsoidal height
     */
    public static Position3d ecef2pos(final Position3d ecefPos) {
        return ecef2pos(ecefPos.getX(), ecefPos.getY(), ecefPos.getZ(), null);
    }



    /**
     * transform geodetic position to ecef position
     * @param lat I latitude (rad, WGS84)
     * @param lon I longitude (rad, WGS84)
     * @param height I ellipsoidal height (m)
     * @param dst O  ecef position {x,y,z} (m)
     */
    static native void _pos2ecef(double lat, double lon, double height, double dst[]);

    /**
     * transform geodetic position to ecef position
     * @param lat I latitude (rad, WGS84)
     * @param lon I longitude (rad, WGS84)
     * @param height I ellipsoidal height (m)
     * @param dst O  ecef position {x,y,z} (m)
     */
    public static Position3d pos2ecef(double lat, double lon, double height, @Nullable Position3d dst) {
        if (dst == null) {
            dst = new Position3d();
        }
        _pos2ecef(lat, lon, height, dst.mPos);
        return dst;
    }


    /**
     * transform ecef covariance to local tangental coordinate
     * @param lat, lon I geodetic position {lat,lon} (rad)
     * @param P I covariance in ecef coordinate
     * @param Q O covariance in local tangental coordinate
     */
    static native void _covenu(double lat, double lon, double[] P, double[] Q);


    /**
     * transform ecef covariance to local tangental coordinate
     * @param lat, lon geodetic position {lat,lon} (rad)
     * @param cov covariance in ecef coordinate
     * @param dst covariance in local tangental coordinate
     * @return dst
     */
    public static Matrix3x3 covenu(double lat, double lon, Matrix3x3 cov, Matrix3x3 dst) {
        if (dst == null) dst = new Matrix3x3();
        _covenu(lat, lon, cov.mMatrix, dst.mMatrix);
        return dst;
    }

    /**
     * transform ecef covariance to local tangental coordinate
     * @param lat, lon geodetic position {lat,lon} (rad)
     * @param cov covariance in ecef coordinate
     * @return covariance in local tangental coordinate
     */
    public static Matrix3x3 covenu(double lat, double lon, Matrix3x3 cov) {
        return covenu(lat, lon, cov, null);
    }


    /**
     * transform ecef vector to local tangental coordinate
     * @param lat, lon I   geodetic position {lat,lon} (rad)
     * @param r I vector in ecef coordinate {x,y,z}
     * @param e O vector in local tangental coordinate {e,n,u}
     */
    static native void _ecef2enu(double lat, double lon, double[] r, double[] e);

    /**
     * transform ecef vector to local tangental coordinate
     * @param lat, lon geodetic position {lat,lon} (rad)
     * @param r vector in ecef coordinate {x,y,z}
     * @param dst vector in local tangental coordinate {e,n,u}
     * @return dst
     */
    public static Position3d ecef2enu(double lat, double lon, Position3d r, Position3d dst) {
        if (dst == null) dst = new Position3d();
        _ecef2enu(lat, lon, r.mPos, dst.mPos);
        return dst;
    }

    /**
     * transform ecef vector to local tangental coordinate
     * @param lat, lon geodetic position {lat,lon} (rad)
     * @param r vector in ecef coordinate {x,y,z}
     * @return vector in local tangental coordinate {e,n,u}
     */
    public static Position3d ecef2enu(double lat, double lon, Position3d r) {
        return ecef2enu(lat, lon, r, null);
    }


    public static class Position3d {
        protected final double mPos[];

        public Position3d() {
            mPos = new double[3];
            mPos[0] = mPos[1] = mPos[2] = 0;
        }

        public Position3d(double values[]) {
            this();
            setValues(values);
        }

        public Position3d(double a, double b, double c) {
            this();
            setValues(a, b, c);
        }

        public Position3d(Position3d src) {
            this();
            setValues(src);
        }

        public double getLat() {
            return mPos[0];
        }

        public double getLon() {
            return mPos[1];
        }

        public double getHeight() {
            return mPos[2];
        }

        public double getX() {
            return mPos[0];
        }

        public double getY() {
            return mPos[1];
        }

        public double getZ() {
            return mPos[2];
        }

        public Position3d setValues(double a, double b, double c) {
            mPos[0] = a;
            mPos[1] = b;
            mPos[2] = c;
            return this;
        }

        public Position3d setValues(double values[]) {
            if (values.length != mPos.length) throw new IllegalArgumentException();
            System.arraycopy(values, 0, mPos, 0, mPos.length);
            return this;
        }

        public Position3d setValues(Position3d src) {
            setValues(src.mPos);
            return this;
        }

        public double[] getValues() {
            return Arrays.copyOf(mPos, mPos.length);
        }

        public void getValues(double[] dst) {
            if (dst.length != mPos.length) throw new IllegalArgumentException();
            System.arraycopy(mPos, 0, dst, 0, mPos.length);
        }

        public double getNorm() {
            return norm(mPos);
        }
    }

    public static class Matrix3x3 {
        protected final double mMatrix[];

        public Matrix3x3() {
            mMatrix = new double[3*3];
        }

        public Matrix3x3(Matrix3x3 src) {
            this();
            setValues(src.mMatrix);
        }

        public Matrix3x3(double values[]) {
            this();
            setValues(values);
        }

        public void setValues(Matrix3x3 src) {
            setValues(src.mMatrix);
        }

        public void setValues(double[] values) {
            if (values.length != mMatrix.length) throw new IllegalArgumentException();
            System.arraycopy(values, 0, this.mMatrix, 0, mMatrix.length);
        }

        public void getValues(double[] dst) {
            if (dst.length != mMatrix.length) throw new IllegalArgumentException();
            System.arraycopy(mMatrix, 0, dst, 0, mMatrix.length);
        }

        public double[] getValues() {
            return Arrays.copyOf(mMatrix, mMatrix.length);
        }
    }

    /**
     * DOP (dilution of precision)
     *
     */
    public static class Dops {

        private final double mDops[];

        public Dops() {
            mDops = new double[4];
        }

        public Dops(double gdop, double pdop, double hdop, double vdop) {
            this();
            setDops(gdop, pdop, hdop, vdop);
        }

        public void copyTo(Dops dst) {
            if (dst == null) throw new IllegalArgumentException();
            System.arraycopy(dst.mDops, 0, mDops, 0, mDops.length);
        }

        // Used in native code
        @Keep
        void setDops(double gdop, double pdop, double hdop, double vdop) {
            mDops[0] = gdop;
            mDops[1] = pdop;
            mDops[2] = hdop;
            mDops[3] = vdop;
        }

        public double getGdop() {
            return mDops[0];
        }

        public double getPdop() {
            return mDops[1];
        }

        public double getHdop() {
            return mDops[2];
        }

        public double getVdop() {
            return mDops[3];
        }
    }


    /**
     * convert degree to degree-minute-second
     */
    public static class Deg2Dms {
        public final double degree;
        public final double minute;
        public final double second;

        public Deg2Dms(double degree) {
            double dd[] = new double[3];
            _deg2dms(degree, dd);
            this.degree = dd[0];
            this.minute = dd[1];
            this.second = dd[2];
        }

        public static String toString(double degree, boolean isLat) {
            double dd[] = new double[3];
            _deg2dms(degree+Math.signum(degree)*1.0E-12, dd);
            return formatString(dd[0], dd[1], dd[2], isLat);
        }

        @Override
        public String toString() {
            return formatString(degree, minute, second, true);
        }

        private static String formatString(double pDegree, double pMinute, double pSecond, boolean isLat) {
            return String.format(
                    Locale.US,
                    "%02.0f° %02.0f' %07.4f″ %s",
                    Math.abs(pDegree),
                    pMinute,
                    pSecond,
                    pDegree > 0.0 ? (isLat ? "N" : "E")  : (isLat ? "S" : "W")
                    );
        }
    }

}
