currentDir=$PWD

echo "更新代码ads_lib..."
cd ..
if [ ! -d "ads_lib" ]; then
git clone git@git.izhifei.com:maozhi/ads_lib.git
fi
cd ads_lib
git pull origin master

cd $currentDir
echo "所有模块代码更新完毕";

