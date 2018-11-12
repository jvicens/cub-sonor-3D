function [] = toMP (id)

directori=strcat('.\IRC_',num2str(id),'\COMPENSATED\WAV\IRC_',num2str(id),'_C\');

delete (strcat(directori,'IRC*MP*'));

% Passem les HRIR a fase mínima

d=dir(directori);

for n=1:length(d)

    if (isempty(findstr(strcat(directori, d(n).name),'.wav')))
        continue;
    end
    
    [y,fs,nbits]=wavread(strcat(directori, d(n).name));

    y_l=transpose(y(:,1));
    [tmp, y_l_mp]=rceps(y_l);
    y_r=transpose(y(:,2));
    [tmp, y_r_mp]=rceps(y_r);
    
    [m,p]=max(xcorr(y_l, y_r));
    ITD=abs(512-p);

    if (not(exist(strcat(directori, 'MP\'), 'file'))) 
        mkdir (strcat(directori, 'MP\'));
    end
    
    name=strcat(directori, 'MP\', strcat(d(n).name(1:26)), '_MP', sprintf('%03d',ITD), '.wav');
    wavwrite ([transpose(y_l_mp) transpose(y_r_mp)], fs, nbits, name);
end

% Dupliquem els 90 graus d'elevació (azimut 0) per tots els azimuts

directori=strcat(directori, 'MP\');

d=dir(directori);

for n=1:length(d)

    if (isempty(findstr(strcat(directori, d(n).name),'_T000_P090_')))
        continue;
    end
    
    [y,fs,nbits]=wavread(strcat(directori, d(n).name));
    break;
end

for s=15:15:345
    
    name=strcat(directori, d(n).name(1:18), sprintf('%03d',s), d(n).name(22:end));
    wavwrite (y, fs, nbits, name);
end

% Dupliquem els 0 graus d'azimut -> 360 graus

d=dir(directori);

for n=1:length(d)

    if (isempty(findstr(strcat(directori, d(n).name),'_T000_')))
        continue;
    end
    
    [y,fs,nbits]=wavread(strcat(directori, d(n).name));
    name=strcat(directori, d(n).name(1:18), sprintf('%03d', 360), d(n).name(22:end));
    wavwrite (y, fs, nbits, name);
end

% Dupliquem els 0 graus d'elevació -> 360 graus

d=dir(directori);

for n=1:length(d)

    if (isempty(findstr(strcat(directori, d(n).name),'_P000_')))
        continue;
    end
    
    [y,fs,nbits]=wavread(strcat(directori, d(n).name));
    name=strcat(directori, d(n).name(1:23), sprintf('%03d', 360), d(n).name(27:end));
    wavwrite (y, fs, nbits, name);
end