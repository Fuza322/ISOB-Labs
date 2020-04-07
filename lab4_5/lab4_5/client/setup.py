# sudo apt install zlib1g-dev
# pip3 install cx-Freeze


from cx_Freeze import setup, Executable

executables = [Executable('client.py', targetName='app.exe')]

include_files = ['data']

options = {
    'build_exe': {
        'include_msvcr': True,
        'build_exe': 'build_windows',
        'include_files': include_files,
    }
}

setup(name='client',
      version='0.0.1',
      description='Client part of application',
      executables=executables,
      options=options
      )

# python3 setup.py buildС
