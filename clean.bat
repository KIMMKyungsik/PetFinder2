del *.iml /a/s
del *.DS_Store /a/s
del *.idea* /a/s
del local.properties
rmdir .gradle /s/q
rmdir .idea /s/q
rmdir build /s/q
rmdir captures /s/q
cd app && rmdir build /s/q
cd..
cd colorpicker && rmdir build /s/q
cd..
cd fab && rmdir build /s/q
cd..
cd instantsearch && rmdir build /s/q
cd..
cd wcviewpager && rmdir build /s/q
cd..
cd viewpagerindicator && rmdir build /s/q
cd..
cd monthandyearpicker && rmdir build /s/q
cd..
pause

