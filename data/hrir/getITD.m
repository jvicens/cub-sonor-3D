origen='.\IRC_1003_C\';

d=dir(origen);

for n=3:length(d)
    [y,fs,nbits]=wavread(strcat(origen,d(n).name));

    y_l=transpose(y(:,1));
    y_r=transpose(y(:,2));
    [m,p]=max(xcorr(y_l, y_r));
    
    azim(n-2)=str2num(d(n).name(19:21));
    elev(n-2)=str2num(d(n).name(24:26));
    ITD(n-2)=abs(512-p);
end
    
    
    