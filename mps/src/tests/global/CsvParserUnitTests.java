package tests.global;

import global.CsvParser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.List;

public class CsvParserUnitTests {

    @Test
    void WHEN_readContent_THEN_returnExpectedValues() {
        String fileContent = """
0.503922,0.625490,0.598039,0.519583,0.621569,0.625729,0.903922,0.625490,0.591996,0.605882,0.700000,0.750980,0.750342,0.860784,0.750980,0.774510
0.001006,0.001006,0.001006,0.001509,0.004025,0.005534,0.008050,0.013584,0.016602,0.019621,0.021633,0.022136,0.024651,0.025657,0.026663,0.026663,0.028675,0.028675,0.029178,0.029178,0.029681,0.030687,0.031190,0.032196,0.032196,0.033202,0.034208,0.035214,0.036220,0.036220,0.037728,0.037728,0.038231,0.040746,0.042255,0.044770,0.047787,0.054324,0.061867,0.069409,0.082481,0.096054,0.116661,0.136761,0.158364,0.190007,0.237201,0.286881,0.349073,0.427759,0.521901,0.630954,0.780833,0.949925,1.145095,1.383559,1.681330,2.038860,2.473166,2.946608,3.475613,4.087275,4.766126,5.497409,6.337467,7.277814,8.357479,9.593539,10.897457,12.361396,13.985386,15.796010,17.638261,19.563688,21.464920,23.499871,25.581610,27.688793,29.893572,32.173120,34.499944,36.806020,39.200417,41.577359,43.979723,46.327546,48.651293,50.965344,53.224332,55.395087,57.551845,59.639762,61.771022,63.855548,65.918628,68.016376,70.067548,72.063559,74.034110,75.929778,77.764538,79.594350,81.319360,83.014409,84.595419,86.082055,87.484715,88.809728,90.046595,91.241074,92.342422,93.363647,94.299952,95.178650,95.990905,96.695851,97.332472,97.887245,98.355676,98.748954,99.094523,99.365310,99.581531,99.753358,99.874687,99.945757,99.978990,99.995346,99.998491,99.997233,99.996101,99.995472,99.994214,99.993963,99.992705,99.991322,99.988178,99.983525,99.979753,99.975604,99.972838,99.968689,99.965170,99.961022,99.958508,99.956246,99.954361,99.951219,99.948832,99.945816,99.943429,99.940413,99.937398,99.935263,99.931871,99.928982,99.925842,99.922576,99.918558,99.914162,99.908387,99.902862,99.896586,99.889180,99.880145,99.866971,99.851292,99.830854,99.807041,99.778105,99.739425,99.689273,99.630805,99.559811,99.475225,99.371278,99.243756,99.082960,98.910131,98.698187,98.448001,98.154342,97.792369,97.373388,96.860052,96.267862,95.582901,94.789253,93.900639,92.878330,91.734708,90.464849,89.058740,87.486932,85.771713,83.867939,81.679566,79.245588,76.522276,73.453198,70.132200,66.636604,63.075540,59.522877,56.070483,52.792678,49.805790,47.056170,44.585109,42.388677,40.425548,38.639548,36.993851,35.456437,33.995260,32.598619,31.300570,30.112620,29.022211,28.026238,27.103358,26.237311,25.412218,24.626440,23.877489,23.166049,22.488047,21.840272,21.222169,20.638072,20.080346,19.550956,19.053825,18.589796,18.153599,17.740945,17.343642,16.958359,16.577251,16.199962,15.826240,15.457534,15.095511,14.746282,14.414381,14.107002,13.826398,13.579985,13.366745,13.190468,13.052608,12.950279,12.878057,12.828003,12.794177,12.737923,
""";
        List<Float> firstLine = List.of(0.503922f, 0.625490f,0.598039f,0.519583f,0.621569f,0.625729f,0.903922f,0.625490f,0.591996f,0.605882f,0.700000f,0.750980f,0.750342f,0.860784f,0.750980f,0.774510f);
        List<Float> secondLine = List.of(0.001006f, 0.001006f, 0.001006f, 0.001509f, 0.004025f, 0.005534f, 0.008050f, 0.013584f, 0.016602f, 0.019621f, 0.021633f, 0.022136f, 0.024651f, 0.025657f, 0.026663f, 0.026663f, 0.028675f, 0.028675f, 0.029178f, 0.029178f, 0.029681f, 0.030687f, 0.031190f, 0.032196f, 0.032196f, 0.033202f, 0.034208f, 0.035214f, 0.036220f, 0.036220f, 0.037728f, 0.037728f, 0.038231f, 0.040746f, 0.042255f, 0.044770f, 0.047787f, 0.054324f, 0.061867f, 0.069409f, 0.082481f, 0.096054f, 0.116661f, 0.136761f, 0.158364f, 0.190007f, 0.237201f, 0.286881f, 0.349073f, 0.427759f, 0.521901f, 0.630954f, 0.780833f, 0.949925f, 1.145095f, 1.383559f, 1.681330f, 2.038860f, 2.473166f, 2.946608f, 3.475613f, 4.087275f, 4.766126f, 5.497409f, 6.337467f, 7.277814f, 8.357479f, 9.593539f, 10.897457f, 12.361396f, 13.985386f, 15.796010f, 17.638261f, 19.563688f, 21.464920f, 23.499871f, 25.581610f, 27.688793f, 29.893572f, 32.173120f, 34.499944f, 36.806020f, 39.200417f, 41.577359f, 43.979723f, 46.327546f, 48.651293f, 50.965344f, 53.224332f, 55.395087f, 57.551845f, 59.639762f, 61.771022f, 63.855548f, 65.918628f, 68.016376f, 70.067548f, 72.063559f, 74.034110f, 75.929778f, 77.764538f, 79.594350f, 81.319360f, 83.014409f, 84.595419f, 86.082055f, 87.484715f, 88.809728f, 90.046595f, 91.241074f, 92.342422f, 93.363647f, 94.299952f, 95.178650f, 95.990905f, 96.695851f, 97.332472f, 97.887245f, 98.355676f, 98.748954f, 99.094523f, 99.365310f, 99.581531f, 99.753358f, 99.874687f, 99.945757f, 99.978990f, 99.995346f, 99.998491f, 99.997233f, 99.996101f, 99.995472f, 99.994214f, 99.993963f, 99.992705f, 99.991322f, 99.988178f, 99.983525f, 99.979753f, 99.975604f, 99.972838f, 99.968689f, 99.965170f, 99.961022f, 99.958508f, 99.956246f, 99.954361f, 99.951219f, 99.948832f, 99.945816f, 99.943429f, 99.940413f, 99.937398f, 99.935263f, 99.931871f, 99.928982f, 99.925842f, 99.922576f, 99.918558f, 99.914162f, 99.908387f, 99.902862f, 99.896586f, 99.889180f, 99.880145f, 99.866971f, 99.851292f, 99.830854f, 99.807041f, 99.778105f, 99.739425f, 99.689273f, 99.630805f, 99.559811f, 99.475225f, 99.371278f, 99.243756f, 99.082960f, 98.910131f, 98.698187f, 98.448001f, 98.154342f, 97.792369f, 97.373388f, 96.860052f, 96.267862f, 95.582901f, 94.789253f, 93.900639f, 92.878330f, 91.734708f, 90.464849f, 89.058740f, 87.486932f, 85.771713f, 83.867939f, 81.679566f, 79.245588f, 76.522276f, 73.453198f, 70.132200f, 66.636604f, 63.075540f, 59.522877f, 56.070483f, 52.792678f, 49.805790f, 47.056170f, 44.585109f, 42.388677f, 40.425548f, 38.639548f, 36.993851f, 35.456437f, 33.995260f, 32.598619f, 31.300570f, 30.112620f, 29.022211f, 28.026238f, 27.103358f, 26.237311f, 25.412218f, 24.626440f, 23.877489f, 23.166049f, 22.488047f, 21.840272f, 21.222169f, 20.638072f, 20.080346f, 19.550956f, 19.053825f, 18.589796f, 18.153599f, 17.740945f, 17.343642f, 16.958359f, 16.577251f, 16.199962f, 15.826240f, 15.457534f, 15.095511f, 14.746282f, 14.414381f, 14.107002f, 13.826398f, 13.579985f, 13.366745f, 13.190468f, 13.052608f, 12.950279f, 12.878057f, 12.828003f, 12.794177f, 12.737923f);

        var contentStream = new ByteArrayInputStream(fileContent.getBytes());
        List<List<Float>> parsedContent = CsvParser.parse(contentStream);

        Assertions.assertEquals(parsedContent.size(), 2);
        Assertions.assertEquals(parsedContent.get(0), firstLine);
        Assertions.assertEquals(parsedContent.get(1), secondLine);
    }

    @Test
    void WHEN_givenBadContent_THEN_throwException() {
        String fileContent = "BAD CONTENT";
        InputStream contentStream = new ByteArrayInputStream(fileContent.getBytes());

        Assertions.assertThrows(Exception.class, () -> CsvParser.parse(contentStream));
    }
}
