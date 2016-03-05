clear

featureStr = {'MATLAB'; 'Image_Toolbox'; 'Wavelet_Toolbox'};
index = cellfun(@(f) license('test',f),featureStr);
availableFeatures = featureStr(logical(index));

% Check that the Image Processing Toolbox is installed
features = strcmp('Image_Toolbox', availableFeatures);

if ~sum(features)
    fprintf('Image Processing Toolbox is missing.\n');
    return
end

% Check that Wavelet Toolbox is installed
features = strcmp('Wavelet_Toolbox', availableFeatures);
if ~sum(index)
    wavInst = true;
else
    wavInst = false;
end

% Set Discrete Wavelet Transform extension mode to
% periodization.
if wavInst
    if ~strcmp(dwtmode('status', 'nodisp'), 'per')
        dwtmode('per', 'nodisp');
    end
end


% Build reference PRNU for the list of FF images
%ReferenceMode('averagePRNU', 'FF', 'out', 'Nexus6', 'FF', 'sigma', 0);

% Build single PRNU for each FF and NI image
%ReferenceMode('singlePRNU', 'FF', 'out', 'Nexus6', 'FF', 'sigma', 0);
%ReferenceMode('singlePRNU', 'NI', 'out', 'Nexus6', 'NI', 'sigma', 0);

% Correlate PRNU for each FF and NI image with the average PRNU from FF
CorrelateMode('out', 'Nexus6_FF_AVE_S_L.mat');
